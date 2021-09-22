from pathlib import Path
import numpy as np
import skimage
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import train_test_split, cross_val_score
from sklearn.neural_network import MLPClassifier
from sklearn.utils import Bunch
from skimage.io import imread
from skimage.transform import resize
import tkinter as tk


def fit_train_set(max_iteration):
    for i in range(max_iteration):
        clf.partial_fit(X_train, y_train, classes=[0, 1, 2, 3, 4, 5, 6, 7, 8, 9])
        pred = clf.predict(X_test)
        print("iteration %d :" % i)
        print("error rate %f" % mean_absolute_error(y_test, pred))


def test():
    print("this will take a few minutes ...")
    scores = cross_val_score(clf, X_test, y_test, cv=5)
    print("Accuracy: %0.2f (+/- %0.2f)" % (scores.mean(), scores.std() * 2))


def load_image_files(container_path, dimension=(64, 64)):
    image_dir = Path(container_path)
    folders = [directory for directory in image_dir.iterdir() if directory.is_dir()]
    categories = [fo.name for fo in folders]

    descr = "A image classification dataset"
    images = []
    flat_data = []
    target = []
    for i, direc in enumerate(folders):
        for file in direc.iterdir():
            img = skimage.io.imread(file)
            img_resized = resize(img, dimension, anti_aliasing=True, mode='reflect')
            flat_data.append(img_resized.flatten())
            images.append(img_resized)
            target.append(i)
    flat_data = np.array(flat_data)
    target = np.array(target)
    images = np.array(images)

    return Bunch(data=flat_data,
                 target=target,
                 target_names=categories,
                 images=images,
                 DESCR=descr)


image_dataset = load_image_files("dataSet")
X_train, X_test, y_train, y_test = train_test_split(
    image_dataset.data, image_dataset.target, test_size=0.3, random_state=109)
clf = MLPClassifier(hidden_layer_sizes=(20, ), activation='tanh', solver='adam', alpha=0.01, batch_size='auto',
                    random_state=1, max_iter=50)
max_iteration = 50

root = tk.Tk()
root.title("Outputs Window")
result = tk.Label(root, text='')
result.grid(row=3, column=0, columnspan=2)
btn1 = tk.Button(root, text='training neural network for image classification : ')
btn1.config(command=lambda: result.config(text=fit_train_set(max_iteration)))
btn1.grid(row=1, column=1, sticky=tk.W, pady=4)

btn2 = tk.Button(root, text='testing neural network for image classification : ')
btn2.config(command=lambda: result.config(text=test()))
btn2.grid(row=2, column=1, sticky=tk.W, pady=4)
root.mainloop()
