import numpy as np
import matplotlib.pyplot as plt
from sklearn.neural_network import MLPRegressor
import random
import math
import tkinter as tk

number_of_points = 1000
tan = 5
random1 = random.random() * tan
random2 = random.random() * tan
x1 = np.random.uniform(-15, 15, size=number_of_points)
np.random.seed(10)
noise = np.random.randn(number_of_points, )
y1 = random1 * x1 + random2 + noise
y2 = np.abs(x1) * (1 / 10) + noise
y3 = np.power(x1, 3) + 1 + noise
y4 = np.sin(x1) + np.cos(x1) + noise
X = np.reshape(x1, [number_of_points, 1])
y1 = np.reshape(y1, [number_of_points, ])
y2 = np.reshape(y2, [number_of_points, ])
y3 = np.reshape(y3, [number_of_points, ])
y4 = np.reshape(y4, [number_of_points, ])

clf1 = MLPRegressor(alpha=0.1, hidden_layer_sizes=(10,), max_iter=1000,
                    activation='logistic', verbose='True', learning_rate='adaptive')
clf2 = MLPRegressor(alpha=0.1, hidden_layer_sizes=(200,), max_iter=1000,
                    activation='relu', verbose='True', learning_rate='adaptive')
clf3 = MLPRegressor(alpha=0.1, hidden_layer_sizes=(20,), max_iter=1000,
                    activation='tanh', verbose='True', learning_rate='adaptive')
clf4 = MLPRegressor(alpha=0.001, hidden_layer_sizes=(20,), max_iter=1000,
                    activation='tanh', verbose='True', learning_rate='adaptive')

root = tk.Tk()
root.title("Outputs Window")
result = tk.Label(root, text='')
result.grid(row=6, column=0, columnspan=2)
btn1 = tk.Button(root, text='Show results for function Y = random1 * X + random2 + noise : ')
btn1.config(command=lambda: result.config(text=clf1.fit(X, y1)))
btn1.grid(row=1, column=1, sticky=tk.W, pady=4)

btn2 = tk.Button(root, text='Show results for function Y = |X|*(1/10) + noise : ')
btn2.config(command=lambda: result.config(text=clf2.fit(X, y2)))
btn2.grid(row=2, column=1, sticky=tk.W, pady=4)

btn3 = tk.Button(root, text='Show results for function Y = X^3 + 1 + noise : ')
btn3.config(command=lambda: result.config(text=clf3.fit(X, y3)))
btn3.grid(row=3, column=1, sticky=tk.W, pady=4)

btn4 = tk.Button(root, text='Show results for function Y = sin(X) + cos(X) + noise : ')
btn4.config(command=lambda: result.config(text=clf4.fit(X, y4)))
btn4.grid(row=4, column=1, sticky=tk.W, pady=4)
root.mainloop()

x_ = np.linspace(-4, 4, 160)  # define axis
pred_x = np.reshape(x_, [160, 1])
pred_y1 = clf1.predict(pred_x)  # predict network output given x
pred_y2 = clf2.predict(pred_x)  # predict network output given x
pred_y3 = clf3.predict(pred_x)  # predict network output given x
pred_y4 = clf4.predict(pred_x)  # predict network output given x
fig = plt.figure()
ax1 = plt.subplot(2, 2, 1)
ax1.plot(x_, random1 * x_ + random2, color='blue')  # plot original function
ax1.plot(pred_x, pred_y1, 'red')  # plot network output
ax2 = plt.subplot(2, 2, 2)
ax2.plot(x_, np.abs(x_) * (1 / 10), color='blue')  # plot original function
ax2.plot(pred_x, pred_y2, 'red')  # plot network output
ax3 = plt.subplot(2, 2, 3)
ax3.plot(x_, np.power(x_, 3) + 1, color='blue')  # plot original function
ax3.plot(pred_x, pred_y3, 'red')  # plot network output
ax4 = plt.subplot(2, 2, 4)
ax4.plot(x_, np.sin(x_) + np.cos(x_), color='blue')  # plot original function
ax4.plot(pred_x, pred_y4, 'red')  # plot network output
plt.show()
