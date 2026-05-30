import base64
import os

def generate_colored_pixel(color_hex):
    # Minimal PNG with specific color
    # This is a bit complex for a script, so I'll use a few predefined base64 for different colors
    colors = {
        "blue": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg==",
        "orange": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+06PkAAAAASUVORK5CYII=",
        "green": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mNk+M8AAf8DAEPh49YAAAAASUVORK5CYII=",
        "red": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mP8z8AAAgMCAO+06PkAAAAASUVORK5CYII=",
        "yellow": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mP8/58BAgsDAn5T9SgAAAAASUVORK5CYII=",
        "gray": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAC1HAwCAAAAC0lEQVR42mP8/5+BAQMDAf8DAn5T9SgAAAAASUVORK5CYII="
    }
    return base64.b64decode(colors.get(color_hex, colors["blue"]))

assets_dir = r"d:\TraeAI\TraePrograme\elm111\elmclient\src\assets"

# Map filenames to colors
image_map = {
    "dcfl01.png": "orange", "dcfl02.png": "yellow", "dcfl03.png": "blue", 
    "dcfl04.png": "red", "dcfl05.png": "green", "dcfl06.png": "orange", 
    "dcfl07.png": "yellow", "dcfl08.png": "blue", "dcfl09.png": "red", "dcfl10.png": "green",
    "sj01.png": "gray", "sj02.png": "gray", "sj03.png": "gray", "sj04.png": "gray", "sj05.png": "gray",
    "index_banner.png": "blue", "super_member.png": "yellow", "alipay.png": "blue", "wechat.png": "green"
}

if not os.path.exists(assets_dir):
    os.makedirs(assets_dir)

for name, color in image_map.items():
    file_path = os.path.join(assets_dir, name)
    with open(file_path, "wb") as f:
        f.write(generate_colored_pixel(color))
    print(f"Generated {color} icon: {file_path}")
