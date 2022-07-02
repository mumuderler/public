#calculate the time for factorisation until 1000

import math
import time
import numpy as np
import matplotlib.pyplot as plt

X_array = []
Y_array = []

for i in range(1,1000,20):
	start = time.process_time_ns()
	y = math.factorial(i)
	end = time.process_time_ns()
	X_array.append(i)
	Y_array.append(end-start)
	print(i, " ",end-start," nanoseconds")
	
#scatter plot to map time required/factorised number
plt.scatter(X_array, Y_array)
x = np.arange(1,1000,1)
m = Y_array[len(Y_array)-1]/ X_array[len(X_array)-1]
y=m*x
plt.plot(y, '-r', label='y=x')

#to compare it to y = x**2
top_end = math.floor(math.sqrt(Y_array[len(Y_array)-1]))
x2 = np.arange(1,top_end,1)
y2 = x2
plt.plot(y2)
plt.title('factorisation time') 
plt.grid()
plt.show()
