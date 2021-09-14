import os
#import pathlib
import sys
sys.path.append("c:\Program Files (x86)\Microsoft Visual Studio\Shared\Python37_64\Lib\pathlib.py")
sys.path.append("c:\Program Files (x86)\Microsoft Visual Studio\Shared\Python37_64\Lib\site-packages\numpy\__init__.py") 
import numpy as np
import tensorflow as tf
from tensorflow.keras import models
from scipy.io import wavfile


os.environ["CUDA_VISIBLE_DEVICES"] = "-1"

model_path='C:/Users/Administrator/Desktop/Covid_model/Model.h5'

samplerate, data = wavfile.read('C:\\Users\Administrator\Desktop\Covid_model\\test\covid\\Covid109_cough-heavy.wav')
zero_padding=np.zeros(794625-len(data))
data=np.concatenate((data,zero_padding))
data=data/32768
data=np.expand_dims(data,-1)
data=np.expand_dims(data,0)

model=models.load_model(model_path)

prediction=model.predict([data])
prediction[prediction >= 0.5] = 1
prediction[prediction < 0.5] = 0
for pred in prediction:
  if pred[0]==1:
    print('Covid')
  else:
    print('Healty')