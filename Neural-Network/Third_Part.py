import numpy as np
from sklearn.neural_network import MLPRegressor
import tkinter as tk

number_of_points = 1000
x1 = np.random.uniform(-20, 20, size=number_of_points)
x2 = np.random.uniform(-20, 20, size=number_of_points)
x3 = np.random.uniform(-20, 20, size=number_of_points)
y1 = np.abs(x1) + np.abs(x2) + np.abs(x3)
y2 = np.sin(x1) + np.cos(x2)
y3 = np.log(np.abs(x1)) + x2
y4 = x1 ** 2 + x2 + np.sin(x3)
X1 = np.reshape((x1, x2), [number_of_points, 2])
X2 = np.reshape((x1, x2, x3), [number_of_points, 3])
y1 = np.reshape(y1, [number_of_points, ])
y2 = np.reshape(y2, [number_of_points, ])
y3 = np.reshape(y3, [number_of_points, ])
y4 = np.reshape(y4, [number_of_points, ])
clf1 = MLPRegressor(alpha=0.000001, hidden_layer_sizes=(300, 2), max_iter=1000,
                    activation='tanh', verbose='True', learning_rate='adaptive')
clf2 = MLPRegressor(alpha=0.00001, hidden_layer_sizes=(300, 2), max_iter=50,
                    activation='tanh', verbose='True', learning_rate='adaptive')
clf3 = MLPRegressor(alpha=0.00001, hidden_layer_sizes=(20, 20, 20), max_iter=500,
                    activation='tanh', verbose='True', learning_rate='adaptive')
clf4 = MLPRegressor(alpha=0.00001, hidden_layer_sizes=(20, 2), max_iter=1000,
                    activation='tanh', verbose='True', learning_rate='adaptive')

root = tk.Tk()
root.title("Outputs Window")
result = tk.Label(root, text='')
result.grid(row=6, column=0, columnspan=2)
btn1 = tk.Button(root, text='Show results for function Y = |x1| + |x2| + |x3| : ')
btn1.config(command=lambda: result.config(text=clf1.fit(X2, y1)))
btn1.grid(row=1, column=1, sticky=tk.W, pady=4)

btn2 = tk.Button(root, text='Show results for function sin(x1) + cos(x2) : ')
btn2.config(command=lambda: result.config(text=clf2.fit(X1, y2)))
btn2.grid(row=2, column=1, sticky=tk.W, pady=4)

btn3 = tk.Button(root, text='Show results for function Y = log(x1) + x2 : ')
btn3.config(command=lambda: result.config(text=clf3.fit(X1, y3)))
btn3.grid(row=3, column=1, sticky=tk.W, pady=4)

btn4 = tk.Button(root, text='Show results for function Y =  x1^2 + x2 + sin(x3): ')
btn4.config(command=lambda: result.config(text=clf4.fit(X2, y4)))
btn4.grid(row=4, column=1, sticky=tk.W, pady=4)
root.mainloop()

x_ = np.linspace(-4, 4, 150)  # define axis
pred_x1 = np.reshape(x_, [75, 2])
pred_x2 = np.reshape(x_, [50, 3])
pred_y1 = clf1.predict(pred_x2)  # predict network output given x
pred_y2 = clf2.predict(pred_x1)  # predict network output given x
pred_y3 = clf3.predict(pred_x1)  # predict network output given x
pred_y4 = clf4.predict(pred_x2)  # predict network output given x
