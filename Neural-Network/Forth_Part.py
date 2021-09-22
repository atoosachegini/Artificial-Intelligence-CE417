import matplotlib.pyplot as plt
from PIL import Image
import numpy as np
from sklearn.neural_network import MLPRegressor
import tkinter as tk

pim = Image.open('function2.png').convert('RGB')
im = np.array(pim)
blue = [0, 0, 255]  # color of higher part of image
Y_temp, X = np.where(np.all(im == blue, axis=2))
X = np.reshape(X, [len(X), 1])
Y = 1000 - Y_temp
Y = np.reshape(Y, [len(X), ])
clf = MLPRegressor(alpha=0.0001, hidden_layer_sizes=(20, 20), max_iter=300,
                   activation='relu', verbose=2, learning_rate='adaptive')
root = tk.Tk()
root.title("Outputs Window")
result = tk.Label(root, text='')
result.grid(row=3, column=0, columnspan=2)
btn = tk.Button(root, text='Show results for your function : ')
btn.config(command=lambda: result.config(text=clf.fit(X, Y)))
btn.grid(row=1, column=1, sticky=tk.W, pady=4)
root.mainloop()

x_ = np.linspace(-4, 4, 160)  # define axis
pred_x = np.reshape(x_, [160, 1])
pred_y = clf.predict(X)  # predict network output given x
plt.scatter(X, Y, color='blue', s=1)  # plot original function
plt.scatter(X, pred_y, color='red', s=1)  # plot network output
plt.show()
