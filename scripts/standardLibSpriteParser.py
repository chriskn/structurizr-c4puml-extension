import json
from collections import OrderedDict
import os
import glob

STANDARD_LIB_FOLDER = "../../plantuml-stdlib/stdlib"
STANDARD_LIB_FOLDER_URL = (
    "https://github.com/plantuml/plantuml-stdlib/tree/master/stdlib"
)

AWS_STANDARD_LIB_FOLDER = "/awslib14/"
AWS_OUTPUT_FILE_PATH = "../src/main/resources/sprites/aws_stdlib_sprites.json"

AZURE_STANDARD_LIB_FOLDER = "/azure/"
AZURE_OUTPUT_FILE_PATH = "../src/main/resources/sprites/azure_stdlib_sprites.json"

ELASTIC_STANDARD_LIB_FOLDER = "/elastic/"
ELASTIC_OUTPUT_FILE_PATH = "../src/main/resources/sprites/elastic_stdlib_sprites.json"

GCP_STANDARD_LIB_FOLDER = "/gcp/"
GCP_OUTPUT_FILE_PATH = "../src/main/resources/sprites/gcp_stdlib_sprites.json"

K8S_STANDARD_LIB_FOLDER = "/k8s/"
K8S_OUTPUT_FILE_PATH = "../src/main/resources/sprites/k8s_stdlib_sprites.json"

CLOUDINSIGHT_STANDARD_LIB_FOLDER = "/cloudinsight/"
CLOUDINSIGHT_OUTPUT_FILE_PATH = (
    "../src/main/resources/sprites/cloudinsight_stdlib_sprites.json"
)


MATERIAL_STANDARD_LIB_FOLDER = "/material/"
MATERIAL_OUTPUT_FILE_PATH = "../src/main/resources/sprites/material_stdlib_sprites.json"

LOGOS_STANDARD_LIB_FOLDER = "/logos/"
LOGOS_OUTPUT_FILE_PATH = "../src/main/resources/sprites/logos_stdlib_sprites.json"

OFFICE_STANDARD_LIB_FOLDER = "/office/"
OFFICE_OUTPUT_FILE_PATH = "../src/main/resources/sprites/office_stdlib_sprites.json"

OSA_STANDARD_LIB_FOLDER = "/osa/"
OSA_OUTPUT_FILE_PATH = "../src/main/resources/sprites/osa_stdlib_sprites.json"

TUPADR3_STANDARD_LIB_FOLDER = "/tupadr3/"
TUPADR3_OUTPUT_FILE_PATH = "../src/main/resources/sprites/tupadr3_stdlib_sprites.json"

# violet
AWS_COLOR_NEBULA = "#C925D1"
# green
AWS_COLOR_ENDOR = "#7AA116"
# orange
AWS_COLOR_SMILE = "#ED7100"
# pink
AWS_COLOR_COSMOS = "#E7157B"
# purple
AWS_COLOR_GALAXY = "#8C4FFF"
# red
AWS_COLOR_MARS = "#DD344C"
# turquoise
AWS_COLOR_ORBIT = "#01A88D"
# Default (blue)
AWS_COLOR_SQUID = "#232F3E"


def elastic_name_function(path):
    name = path.replace("/", "-")
    deduplicated_name = "-".join(OrderedDict.fromkeys(name.split("-")))
    return deduplicated_name.replace("_", "-")


def default_name_function(path):
    return path.replace("/", "-").replace("_", "-")


def k8s_name_function(path):
    return default_name_function(path).replace("OSS-", "")


def aws_name_function(path):
    return default_name_function(path).replace("lib14", "")


def aws_color_by_group(group):
    color = AWS_COLOR_SQUID
    if group in ["Customer", "Enablement", "Database", "DeveloperTools", "Satellite"]:
        color = AWS_COLOR_NEBULA
    elif group in ["CloudFinancialManagement", "InternetOfThings", "Storage"]:
        color = AWS_COLOR_ENDOR
    elif group in [
        "Blockchain",
        "Compute",
        "Containers",
        "Media Services",
        "QuantumTechnologies",
    ]:
        color = AWS_COLOR_SMILE
    elif group in ["ApplicationIntegration", "ManagementGovernance"]:
        color = AWS_COLOR_COSMOS
    elif group in ["Analytics", "Games", "NetworkingContentDelivery", "Serverless"]:
        color = AWS_COLOR_GALAXY
    elif group in [
        "BusinessApplications",
        "ContactCenter",
        "FrontEndWebMobile",
        "Robotics",
        "Security",
        "Identity",
        "Compliance",
    ]:
        color = AWS_COLOR_MARS
    elif group in ["EndUserComputing", "MachineLearning", "MigrationTransfer"]:
        color = AWS_COLOR_ORBIT
    return color


def write_sprite_set(
    output_file,
    sprite_set_name,
    sprite_set_source,
    json_sprites,
    additional_includes,
):
    with open(output_file, "w") as f:
        sprite_set = {}
        sprite_set["name"] = sprite_set_name
        sprite_set["source"] = sprite_set_source
        if additional_includes:
            sprite_set["additionalIncludes"] = additional_includes
        sprite_set["sprites"] = json_sprites
        f.write(json.dumps(sprite_set))


def create_sprites(paths, color_function, name_function, reference_function):
    json_sprites = []
    for path in paths:
        category = path.split("/")[1]
        sprite = {}
        sprite["@type"] = "PlantUmlSprite"
        sprite["name"] = name_function(path)
        sprite["path"] = "<" + path + ">"
        if reference_function:
            sprite["reference"] = reference_function(path)
        if color_function:
            sprite["color"] = color_function(category)
        json_sprites.append(sprite)
    return json_sprites


def parse_paths(path_to_icon_folder, fileending):
    paths = []
    for full_path in glob.iglob(
        path_to_icon_folder + "**/**/*[!LARGE|all]*" + fileending, recursive=True
    ):
        relativ_path = full_path.replace(STANDARD_LIB_FOLDER, "")
        norm_path = os.path.normpath(relativ_path)
        icon_path = norm_path.replace(os.sep, "/").replace(fileending, "")
        paths.append(icon_path[1:])
    return paths


def process_sprite_folder(
    sprite_source_folder,
    output_file,
    sprite_set_name,
    sprite_fileending=".puml",
    additional_includes=None,
    color_function=None,
    name_function=default_name_function,
    reference_function=None,
):
    paths = parse_paths(STANDARD_LIB_FOLDER + sprite_source_folder, sprite_fileending)
    json_sprites = create_sprites(
        paths=paths,
        color_function=color_function,
        name_function=name_function,
        reference_function=reference_function,
    )
    write_sprite_set(
        output_file=output_file,
        sprite_set_name=sprite_set_name,
        sprite_set_source=STANDARD_LIB_FOLDER_URL + sprite_source_folder,
        json_sprites=json_sprites,
        additional_includes=additional_includes,
    )


if __name__ == "__main__":
    process_sprite_folder(
        sprite_source_folder=AWS_STANDARD_LIB_FOLDER,
        output_file=AWS_OUTPUT_FILE_PATH,
        sprite_set_name="AWS plantuml-stdlib Sprites",
        additional_includes=["<awslib14/AWSCommon>"],
        color_function=aws_color_by_group,
        name_function=aws_name_function,
    )

    process_sprite_folder(
        sprite_source_folder=AZURE_STANDARD_LIB_FOLDER,
        output_file=AZURE_OUTPUT_FILE_PATH,
        sprite_set_name="Azure plantuml-stdlib Sprites",
        additional_includes=["<azure/AzureCommon>"],
        color_function=lambda _: "#0072C6",
    )

    process_sprite_folder(
        sprite_source_folder=ELASTIC_STANDARD_LIB_FOLDER,
        output_file=ELASTIC_OUTPUT_FILE_PATH,
        sprite_set_name="Elastic plantuml-stdlib Sprites",
        color_function=lambda _: "#51D0C8",
        sprite_fileending="-sprite.puml",
        name_function=elastic_name_function,
    )

    process_sprite_folder(
        sprite_source_folder=GCP_STANDARD_LIB_FOLDER,
        output_file=GCP_OUTPUT_FILE_PATH,
        sprite_set_name="Google Cloud Platform plantuml-stdlib Sprites",
        additional_includes=["<gcp/GCPCommon>"],
        color_function=lambda _: "#79B3FE",
    )

    process_sprite_folder(
        sprite_source_folder=K8S_STANDARD_LIB_FOLDER,
        output_file=K8S_OUTPUT_FILE_PATH,
        sprite_set_name="Kubernetes plantuml-stdlib Sprites",
        additional_includes=["<k8s/Common>"],
        color_function=lambda _: "#66ABDD",
        name_function=k8s_name_function,
    )

    process_sprite_folder(
        sprite_source_folder=CLOUDINSIGHT_STANDARD_LIB_FOLDER,
        output_file=CLOUDINSIGHT_OUTPUT_FILE_PATH,
        sprite_set_name="Cloudinsight plantuml-stdlib Sprites",
        color_function=lambda _: "#23a3dd",
    )

    process_sprite_folder(
        sprite_source_folder=MATERIAL_STANDARD_LIB_FOLDER,
        output_file=MATERIAL_OUTPUT_FILE_PATH,
        sprite_set_name="Google Material plantuml-stdlib Sprites",
        reference_function=lambda path: "ma_" + path.split("/")[-1],
    )

    process_sprite_folder(
        sprite_source_folder=LOGOS_STANDARD_LIB_FOLDER,
        output_file=LOGOS_OUTPUT_FILE_PATH,
        sprite_set_name="Logos plantuml-stdlib Sprites",
    )

    process_sprite_folder(
        sprite_source_folder=OFFICE_STANDARD_LIB_FOLDER,
        output_file=OFFICE_OUTPUT_FILE_PATH,
        sprite_set_name="Office plantuml-stdlib Sprites",
    )

    process_sprite_folder(
        sprite_source_folder=OSA_STANDARD_LIB_FOLDER,
        output_file=OSA_OUTPUT_FILE_PATH,
        sprite_set_name="Open Security Architecture plantuml-stdlib Sprites",
        sprite_fileending="-sprite.puml",
    )

    process_sprite_folder(
        sprite_source_folder=TUPADR3_STANDARD_LIB_FOLDER,
        output_file=TUPADR3_OUTPUT_FILE_PATH,
        sprite_set_name="TUPADR3 plantuml-stdlib Sprites",
    )
