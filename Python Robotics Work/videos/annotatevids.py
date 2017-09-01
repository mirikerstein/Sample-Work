import cv2 
import glob
import subprocess
from ast import literal_eval

# -------------------------------------------------- 
# draws a rectange on an image at given position 
def getAnnotateFrame(img, pos):
    newimg = img.copy() 
    minx, maxx = int(pos[0] - 10), int(pos[0] + 10)
    miny, maxy = int(pos[1] - 10), int(pos[1] + 10)
    cv2.rectangle(newimg, (minx,miny), (maxx,maxy), (200,255,100), 2)
    return newimg 

# -------------------------------------------------- 
# draws rectanges on image sequence given positions 
def getAnnotatedFrames(frames, positions): 
    annotations = []
    for i in range(min(len(frames), len(positions))): 
        annotated = getAnnotateFrame(frames[i], positions[i])
        annotations.append(annotated) 
    return annotations

# -------------------------------------------------- 
# get paths for all images of extension at folder dir  
def getImagePaths(imagesRoot, extension):
    images = glob.glob(imagesRoot + "*."+extension)
    names = []
    for i in images:
        names.append(i.replace(imagesRoot, ""))
    return images, names 

# -------------------------------------------------- 
# get cv2 images given paths for images 
def getImages(imagePaths):
    images = []
    for path in imagePaths:
        images.append(cv2.imread(path))
    return images 

# main function, called only if this file gets run 
if __name__ == "__main__":

    # ===============================================================
    # sample usage, replace string paths accordingly 
    # ===============================================================

    IMGS_ROOT = "/path/for/images/root/folder/here/" # Must end with slash
    INPUT_TXT = "/path/for/input/positions/file.txt"
    OUTPUT_ROOT = "/path/to/output/" # Must end with slash

    imagePaths, names = getImagePaths(IMGS_ROOT, "jpg")
    images = getImages(imagePaths)

    input_file = open(INPUT_TXT, 'r')
    inputs = [literal_eval(l.strip()) for l in input_file.readlines()]
    input_file.close()

    annotations = getAnnotatedFrames(images, inputs)
    for i in range(len(annotations)):
        cv2.imwrite(OUTPUT_ROOT + ("00000" + str(i))[-6:] + ".jpg", annotations[i])

    subprocess.call(["ffmpeg", "-y", "-i", OUTPUT_ROOT + "%06d.jpg", "-pix_fmt", "yuv420p", "output.m4v"])

