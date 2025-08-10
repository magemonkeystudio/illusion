# The purpose of this script is to copy files from a source directory to a new directory, change all the package names in the copied files,
# and then add the applicable module to the appropriate pom.xml files
import os
import re
import shutil
from lxml import etree


def copy_and_rename_files(src_dir, dest_dir, old_package, new_package):
    if not os.path.exists(dest_dir):
        os.makedirs(dest_dir)

    for root, dirs, files in os.walk(src_dir):
        for file in files:
            if file.endswith('.java') or file.endswith('.xml'):
                src_file = os.path.join(root, file)
                rel_path = os.path.relpath(src_file, src_dir)
                dest_file = os.path.join(dest_dir, rel_path)

                # Create the destination directory if it doesn't exist
                dest_file_dir = os.path.dirname(dest_file)
                if not os.path.exists(dest_file_dir):
                    os.makedirs(dest_file_dir)

                # Copy the file
                shutil.copy2(src_file, dest_file)

                # Rename package in the copied file
                with open(dest_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                content = re.sub(r'\b' + re.escape(old_package) + r'\b', new_package, content)

                with open(dest_file, 'w', encoding='utf-8') as f:
                    f.write(content)
                    # Rename directory in dest_dir if it matches the old package's last segment

    old_segment = old_package.split('.')[-1]
    new_segment = new_package.split('.')[-1]
    for root, dirs, files in os.walk(dest_dir, topdown=False):
        for d in dirs:
            if d == old_segment:
                old_path = os.path.join(root, d)
                new_path = os.path.join(root, new_segment)
                if not os.path.exists(new_path):
                    os.rename(old_path, new_path)


def update_pom_files(src_dir, dest_dir, old_package, new_package):
    src_version = src_dir.split('/')[-1]
    dest_version = dest_dir.split('/')[-1]
    old_version = old_package.split('.')[-1]
    new_version = new_package.split('.')[-1]
    for root, dirs, files in os.walk(dest_dir):
        for file in files:
            if file == 'pom.xml':
                # The main pom here, we just need to replace any references of the src_dir's last path piece (`v1_21_r4`) with the dest_dir's (`v1_21_r5`)
                pom_file = os.path.join(root, file)
                content = ''
                with open(pom_file, 'r', encoding='utf-8') as f:
                    content = f.read()

                print(
                    f"Updating {pom_file} from {src_version} to {dest_version} and package from {old_version} to {new_version}")
                content = content.replace(src_version, dest_version).replace(old_version, new_version)

                with open(pom_file, 'w', encoding='utf-8') as f:
                    f.write(content)

    # Now we need to update the parent module's pom to include the new module (as well as other poms)
    poms_to_update = [
        os.path.join(dest_dir, '..', 'pom.xml'),
        os.path.join(dest_dir, '..', '..', 'core', 'pom.xml'),
        os.path.join(dest_dir, '..', '..', 'pom.xml'),
        os.path.join(dest_dir, '..', '..', 'pom-dev.xml')
    ]
    node_to_update = [
        'modules',
        'dependencies',
        'dependencyManagement/dependencies',
        'dependencyManagement/dependencies'
    ]
    for pom_path in poms_to_update:
        index = poms_to_update.index(pom_path)
        if os.path.exists(pom_path):
            parser = etree.XMLParser(remove_blank_text=True)
            tree = etree.parse(pom_path, parser)
            root = tree.getroot()
            nsmap = root.nsmap
            ns = nsmap.get(None)  # Default namespace

            def qname(tag):
                return f"{{{ns}}}{tag}" if ns else tag

            node = node_to_update[index]
            target_node = root.find(qname(node))
            if node == 'dependencyManagement/dependencies':
                # For dependencyManagement/dependencies, we need to ensure the parent exists
                dep_management = root.find(qname('dependencyManagement'))
                if dep_management is None:
                    dep_management = etree.SubElement(root, qname('dependencyManagement'))
                target_node = dep_management.find(qname('dependencies'))
            if target_node is None:
                print(f'Target node {node_to_update[index]} not found in {pom_path}')
                continue

            if node == 'modules':
                module_names = [m.text for m in target_node.findall(qname('module'))]
                if dest_version not in module_names:
                    print(f"Adding {dest_version} to {pom_path}")
                    new_module = etree.SubElement(target_node, qname('module'))
                    new_module.text = dest_version
                    etree.indent(tree, space="    ")
                    tree.write(pom_path, pretty_print=True, xml_declaration=True, encoding='utf-8')
                else:
                    print(f"{dest_version} is already included in {pom_path}")
            elif node == 'dependencies':
                dependency_names = [d.find(qname('artifactId')).text for d in target_node.findall(qname('dependency'))]
                if 'illusion-nms-' + dest_version not in dependency_names:
                    print(f"Adding {new_package} to {pom_path}")
                    new_dependency = etree.SubElement(target_node, qname('dependency'))
                    etree.SubElement(new_dependency, qname('groupId')).text = '${project.groupId}'
                    etree.SubElement(new_dependency, qname('artifactId')).text = 'illusion-nms-' + dest_version
                    etree.indent(tree, space="    ")
                    tree.write(pom_path, pretty_print=True, xml_declaration=True, encoding='utf-8')
                else:
                    print(f"{new_package} is already included in {pom_path}")
            elif node == 'dependencyManagement/dependencies':
                dependency_names = [d.find(qname('artifactId')).text for d in target_node.findall(qname('dependency'))]
                if 'illusion-nms-' + dest_version not in dependency_names:
                    print(f"Adding {new_package} to {pom_path}")
                    new_dependency = etree.SubElement(target_node, qname('dependency'))
                    etree.SubElement(new_dependency, qname('groupId')).text = '${project.groupId}'
                    etree.SubElement(new_dependency, qname('artifactId')).text = 'illusion-nms-' + dest_version
                    etree.SubElement(new_dependency, qname('version')).text = '${nms.version}'
                    etree.indent(tree, space="    ")
                    tree.write(pom_path, pretty_print=True, xml_declaration=True, encoding='utf-8')
                else:
                    print(f"{new_package} is already included in {pom_path}")


def main():
    src_dir = 'nms/v1_21_r4'
    dest_dir = 'nms/v1_21_r5'
    old_package = 'me.filoghost.holographicdisplays.nms.v1_21_R4'
    new_package = 'me.filoghost.holographicdisplays.nms.v1_21_R5'

    copy_and_rename_files(src_dir, dest_dir, old_package, new_package)
    update_pom_files(src_dir, dest_dir, old_package, new_package)


if __name__ == '__main__':
    main()
