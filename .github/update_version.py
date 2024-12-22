import os
import re
import sys

is_dev = len(sys.argv) >= 2 and eval(sys.argv[1].lower().capitalize())


def replace_version(pom_path):
    regex = r'(<artifactId>.*illusion.*<\/artifactId>\s*)<version>(((\d+\.?)+)(-SNAPSHOT)?)<\/version>$'
    with open(pom_path, 'r') as pom:
        contents = pom.read()
        ver = re.findall(regex, contents, re.MULTILINE)
        if not ver:
            print(f'No version found in {pom_path}')
            return

        version = ver[0][1]
        bare_version = ver[0][2]
        if is_dev:
            split = bare_version.split('.')
            if '-SNAPSHOT' in version:
                split[-1] = str(int(split[-1]) + 1)
            else:
                split[-1] = '0'
            version = '.'.join(split)
            new_version = version + '-SNAPSHOT'
        else:
            split = version.split('.')
            split[-2] = str(int(split[-2]) + 1)
            split[-1] = '0'
            version = '.'.join(split)
            new_version = version;
        contents = re.sub(regex,
                          ver[0][0] + '<version>' + new_version + '</version>',
                          contents,
                          1,
                          re.MULTILINE)
    with open(pom_path, 'w') as pom:
        pom.write(contents)


def find_pom_files(directory):
    pom_files = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file == 'pom.xml' or file == 'pom-dev.xml':
                pom_files.append(os.path.join(root, file))
    return pom_files


if __name__ == "__main__":
    directory = os.getcwd()
    pom_files = find_pom_files(directory)
    for pom_file in pom_files:
        # NMS versions should be updated manually when NMS is actually changed
        if 'nms' in pom_file: continue
        print(f'Updating version in {pom_file}')
        replace_version(pom_file)
