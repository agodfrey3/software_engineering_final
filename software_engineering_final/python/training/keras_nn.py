import os
os.environ["CUDA_DEVICE_ORDER"] = "PCI_BUS_ID"   # see issue #152
os.environ["CUDA_VISIBLE_DEVICES"] = "1"


import pandas as pd
import numpy as np
from keras.models import Sequential
from keras.models import model_from_json
from keras.layers import Dense, Dropout
from keras.wrappers.scikit_learn import KerasRegressor
from sklearn.model_selection import cross_val_score
from sklearn.model_selection import KFold
from sklearn.preprocessing import StandardScaler
from sklearn.pipeline import Pipeline
import tensorflow as tf
from sklearn.model_selection import GridSearchCV
from software_engineering_final.python.file_helpers.file_helpers import save_pkl_file

from software_engineering_final.python.data_helpers.regression_helpers import format_X_y

seed = 7


def create_model(neurons: int=9,
                 num_dense: int=1,
                 k_initializer: str='normal',
                 dropout_rate: float=0.2,
                 optimizer: str='adam',
                 loss: str='mean_squared_error',
                 activation: str='relu'):

    # create model
    model = Sequential()
    model.add(Dense(neurons, input_dim=9, kernel_initializer=k_initializer, activation=activation))
    model.add(Dropout(dropout_rate))
    for i in range(num_dense - 1):
        model.add(Dense(neurons, kernel_initializer=k_initializer))
    model.add(Dense(1, kernel_initializer=k_initializer))
    model.compile(loss=loss, optimizer=optimizer)

    return model


def save_pipeline(pipeline, save_dir: str=".", save_name: str="baseline"):

    print("Saving pipeline to {save_dir}...")

    json_model = pipeline.named_steps['mlp'].model.to_json()
    open(f"{save_dir}/model_architecture_{save_name}.json", 'w').write(json_model)

    pipeline.named_steps['mlp'].model.save_weights(f"{save_dir}/model_weights_{save_name}.h5", overwrite=True)


def load_model(json_path: str, h5_path: str, model_name: str='mlp'):

    model = model_from_json(open(json_path).read())
    model.load_weights(h5_path)

    model.compile(loss='mean_squared_error', optimizer='adam')

    return model


def train_standardized(X, y, model_name: str="baseline"):

    with tf.device("/device:GPU:0"):

        # evaluate model with standardized dataset
        np.random.seed(seed)

        estimators = []
        # estimators.append(('standardize', StandardScaler()))
        estimators.append(('mlp', KerasRegressor(build_fn=baseline_model, epochs=1000, batch_size=256, verbose=0)))
        pipeline = Pipeline(estimators)

        pipeline.fit(X, y)

        save_pipeline(pipeline, save_name=model_name)

        return pipeline


def get_average_error(model, validation_path: str, validation_name: str):

    num_checked = 0
    total_dif = 0

    num_greater = 0
    num_less = 0

    data_df = pd.read_csv(validation_path)

    for i in range(len(data_df) - 1):

        test_df = data_df.iloc[i:i + 1]

        X_valid, y_valid = format_X_y(test_df)
        y_pred = model.predict(X_valid)

        dif = abs(y_pred - y_valid) / y_valid
        total_dif = total_dif + dif

        if y_pred > y_valid:
            num_greater += 1
        elif y_pred < y_valid:
            num_less += 1

        num_checked = num_checked + 1

    average_dif = total_dif / num_checked

    print(f"         Model: {validation_name}\n"
          f"Average error : {average_dif}\n"
          f"   Num greater: {num_greater}\n"
          f"     Num less : {num_less}\n"
          f"   Sample size: {num_checked}")


if __name__ == '__main__':

    data_dir = "C:/Users/Andrew/data/stock_data/formatted"
    train_data_path = os.path.join(data_dir, "training/dow_30_time_series_5.csv")

    # validation_path = "C:/Users/Andrew/data/stock_data/formatted/ba_validation.csv"
    validation_path = "c:/users/andrew/data/stock_data/formatted/validation/justin_validation_chunk_5.csv"

    df = pd.read_csv(train_data_path)
    X, y = format_X_y(df)

    # epochs = [50, 100, 250, 500, 1000]
    # batch_size = [64, 128, 256, 512]
    # optimizer = ['RMSprop', 'Adam']
    # activation = ['relu', 'sigmoid', 'linear', 'softmax']
    # dropout_rate = [0.0, 0.1, 0.2, 0.3, 0.4]
    # neurons = [1, 5, 10, 15, 20, 25, 30]
    # num_dense = [1, 3, 5, 7, 9]

    epochs = [50]
    batch_size = [512]
    optimizer = ['Adam']
    activation = ['linear']
    dropout_rate = [0.2]
    neurons = [30]
    dense = [5]

    param_grid = dict(epochs=epochs,
                      batch_size=batch_size,
                      optimizer=optimizer,
                      activation=activation,
                      dropout_rate=dropout_rate,
                      neurons=neurons,
                      num_dense=num_dense
                      )

    model = KerasRegressor(build_fn=create_model, verbose=2)

    grid = GridSearchCV(estimator=model, param_grid=param_grid, n_jobs=-1)
    grid_result = grid.fit(X, y)

    print("Best: %f using %s" % (grid_result.best_score_, grid_result.best_params_))
    means = grid_result.cv_results_['mean_test_score']
    stds = grid_result.cv_results_['std_test_score']
    params = grid_result.cv_results_['params']
    for mean, stdev, param in zip(means, stds, params):
        print("%f (%f) with: %r" % (mean, stdev, param))



