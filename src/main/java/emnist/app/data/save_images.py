import io
import os
import sys
import pandas as pd
from PIL import Image

dir_path = os.path.dirname(os.path.realpath(__file__))
os.makedirs('src\\main\\java\\emnist\\app\\data\\train\\', exist_ok = True)
os.makedirs('src\\main\\java\\emnist\\app\\data\\test\\', exist_ok = True)

# PRINT PROGRESS BAR TO CONSOLE
def printProgressBar (iteration, total, length = 50):
    prefix = 'Progress:'
    suffix = 'Complete'
    percent = ("{0:." + str(1) + "f}").format(100 * (iteration / float(total)))
    filledLength = int(length * iteration // total)
    bar = '█' * filledLength + '-' * (length - filledLength)
    sys.stdout.write(f'\r{prefix} |{bar}| {percent}% {suffix}')
    sys.stdout.flush()
    # Print New Line on Complete
    if iteration == total: 
        print()

def main():
    # INITIALIZE PROGRESS BAR
    num_complete = 0
    print('\nSaving EMNIST images as \"PNG\" files')
    printProgressBar(0, 70000)

    # GET TRAINING EMNIST IMAGES FROM PARQUET AND SAVE TO PNG FILES
    train_dataframe = pd.read_parquet(dir_path + '\\train.parquet', engine = 'pyarrow')
    for i in range(len(train_dataframe.index)):
        # GET IMAGE DATA
        bytes = train_dataframe.iloc[i].image['bytes']
        stream = io.BytesIO(bytes)
        image = Image.open(stream).transpose(Image.FLIP_LEFT_RIGHT).rotate(90)

        # EMNIST DATA LABEL    
        # label = test_dataframe.iloc[i].label

        # SAVE IMAGE
        file_name = str(i).zfill(5) + ".png"
        image.save('src\\main\\java\\emnist\\app\\data\\train\\' + file_name)

        num_complete = num_complete + 1
        printProgressBar(num_complete, 70000)

    # GET TESTING EMNIST IMAGES FROM PARQUET AND SAVE TO PNG FILES
    test_dataframe = pd.read_parquet(dir_path + '\\test.parquet', engine = 'pyarrow')
    for i in range(len(test_dataframe.index)):
        # GET IMAGE DATA
        bytes = test_dataframe.iloc[i].image['bytes']
        stream = io.BytesIO(bytes)
        image = Image.open(stream).transpose(Image.FLIP_LEFT_RIGHT).rotate(90)

        # EMNIST DATA LABEL    
        # label = test_dataframe.iloc[i].label

        # SAVE IMAGE
        file_name = str(i).zfill(5) + ".png"
        image.save('src\\main\\java\\emnist\\app\\data\\test\\' + file_name)

        num_complete = num_complete + 1
        printProgressBar(num_complete, 70000)

if __name__ == "__main__":
    main()