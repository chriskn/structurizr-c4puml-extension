import re
import json
import urllib.request

BASE_PATH = (
    "https://raw.githubusercontent.com/plantuml-stdlib/gilbarbara-plantuml-sprites"
)
GILBARBARA_ICON_URL = BASE_PATH + "/v1.1"
SPRITE_FILE_URL = BASE_PATH + "/refs/heads/master/sprites-list.md"
OUTPUT_FILE_JSON = "../src/main/resources/sprites/gilbarbara_image_sprites.json"
URL_VAR_NAME = "GILBARBARA_PNG_URL"
ADDITIOANL_DEFINITIONS = [URL_VAR_NAME + " " + GILBARBARA_ICON_URL + "/pngs"]


def icon_names_to_path():
    sprite_defs = urllib.request.urlopen(SPRITE_FILE_URL).readlines()
    index_to_start = sprite_defs.index(b"|--------|------|\n") + 1
    icon_name_to_path = {}
    for sprite_def in sprite_defs[index_to_start:]:
        sprite_values = sprite_def.decode("utf-8").replace("🧟", "").split("|")
        name = sprite_values[1].strip() + "-img"
        icon = sprite_values[2].strip()
        icon_path = re.search(r"\((.+)\)", icon).group(1).replace("pngs", "")
        icon_path = "img:" + GILBARBARA_ICON_URL + icon_path
        icon_name_to_path[name] = icon_path
    return icon_name_to_path


if __name__ == "__main__":
    json_sprites = []
    for name, url in icon_names_to_path().items():
        json_sprites.append(
            {
                "@type": "ImageSprite",
                "name": "logos-" + name,
                "url": url.replace(GILBARBARA_ICON_URL, URL_VAR_NAME),
            }
        )

    sprite_set = {}
    sprite_set["name"] = "gilbarbara PNG sprites"
    sprite_set["source"] = GILBARBARA_ICON_URL
    sprite_set["additionalDefinitions"] = ADDITIOANL_DEFINITIONS
    sprite_set["sprites"] = json_sprites
    with open(OUTPUT_FILE_JSON, "w") as f:
        f.write(json.dumps(sprite_set))
