from pathlib import Path
import numpy as np
import skimage
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
from sklearn.utils import Bunch
from skimage.io import imread
from skimage.transform import resize
from tensorflow.python.keras import Sequential
from tensorflow.python.keras.layers import Dense
import tkinter as tk


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
X_train, X_test = train_test_split(
    image_dataset.data, test_size=0.3, random_state=109)

sigma = 0.7
noisy_train = np.clip(X_train + np.random.normal(0, sigma, X_train.shape), 0, 1)
noisy_test = np.clip(X_test + np.random.normal(0, sigma, X_test.shape), 0, 1)

model = Sequential()
model.add(Dense(64, input_dim=64 * 64, activation='relu'))
model.add(Dense(64 * 64, activation='relu'))
model.compile(loss='mse', optimizer='adam')
root = tk.Tk()
root.title("Outputs Window")
result = tk.Label(root, text='')
result.grid(row=4, column=0, columnspan=2)
btn1 = tk.Button(root, text='training neural network for image denoising : ')
btn1.config(command=lambda: result.config(
    text=model.fit(noisy_train, X_train, batch_size=512, epochs=100, validation_split=0.2, verbose=2)))
btn1.grid(row=1, column=1, sticky=tk.W, pady=4)

btn2 = tk.Button(root, text='testing neural network for image denoising : ')
btn2.config(command=lambda: result.config(
    text=model.evaluate(noisy_test, X_test)))
btn2.grid(row=2, column=1, sticky=tk.W, pady=4)
root.mainloop()

randoms = np.random.choice(np.arange(len(X_test)), size=10)
images = np.zeros((10, 3, X_test[0].shape[0]))
images[:, 0] = X_test[randoms]
images[:, 1] = noisy_test[randoms]
images[:, 2] = model.predict(images[:, 1])
images = images.reshape(10 * 3, X_test[0].shape[0], )

fig = plt.figure(figsize=(24, 20))

for i in range(30):
    ax = fig.add_subplot(10, 3, i + 1)
    ax.set_aspect('equal')
    ax.get_xaxis().set_visible(False)
    ax.get_yaxis().set_visible(False)
    plt.imshow(images[i].reshape(64, 64) * 255)
plt.show()
