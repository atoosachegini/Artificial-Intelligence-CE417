from pathlib import Path
import numpy as np
import skimage
from sklearn import metrics
from sklearn.metrics import classification_report
from sklearn.svm import SVC
from sklearn.utils import Bunch
from sklearn.model_selection import train_test_split
from skimage.io import imread
from skimage.transform import resize


def load_image_files(container_path, dimension=(64, 64)):
    image_dir = Path(container_path)
    folders = [directory for directory in image_dir.iterdir() if directory.is_dir()]
    categories = [fo.name for fo in folders]

    descr = "Image classification dataSet"
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


image_dataset1 = load_image_files("dataset2/2,7")
X_train, X_test, y_train, y_test = train_test_split(
    image_dataset1.data, image_dataset1.target, test_size=0.35, random_state=109)
names = ["Linear SVM", "RBF SVM", "Poly SVM", "Sigmoid SVM"]
classifiers = [
    SVC(kernel="linear", C=1),
    SVC(gamma=1, C=1),
    SVC(kernel="poly", C=1),
    SVC(kernel="sigmoid", gamma=2)]

i = 1
for name, clf in zip(names, classifiers):
    clf.fit(X_train, y_train)
    score = clf.score(X_test, y_test)
    print("2,7 Detection")
    print(name)
    y_pred = clf.predict(X_test)
    print(classification_report(y_test, y_pred))
    print("Accuracy:", metrics.accuracy_score(y_test, y_pred))
    i += 1

image_dataset2 = load_image_files("dataset2/2,3")
X_train, X_test, y_train, y_test = train_test_split(
    image_dataset2.data, image_dataset2.target, test_size=0.35, random_state=109)
i = 1
for name, clf in zip(names, classifiers):
    clf.fit(X_train, y_train)
    score = clf.score(X_test, y_test)
    print("2,3 Detection")
    print(name)
    y_pred = clf.predict(X_test)
    print(classification_report(y_test, y_pred))
    print("Accuracy:", metrics.accuracy_score(y_test, y_pred))
    i += 1

image_dataset3 = load_image_files("dataset2/SW")
X_train, X_test, y_train, y_test = train_test_split(
    image_dataset3.data, image_dataset3.target, test_size=0.35, random_state=109)

i = 1
for name, clf in zip(names, classifiers):
    clf.fit(X_train, y_train)
    score = clf.score(X_test, y_test)
    print("W,S Detection")
    print(name)
    y_pred = clf.predict(X_test)
    print(classification_report(y_test, y_pred))
    print("Accuracy:", metrics.accuracy_score(y_test, y_pred))
    i += 1
