import numpy as np
import pandas as pd

from ast import literal_eval
from sklearn.model_selection import train_test_split
from sklearn import preprocessing

from software_engineering_final.file_helpers.file_helpers import load_pkl_file
from software_engineering_final.training.training_helpers import plot_results, get_metrics
from software_engineering_final.training.get_regressor import regressor_dict
from software_engineering_final.data_helpers.regression_helpers import time_series_to_X, format_X_y


def main():
    data_path = "C:/Users/God/data/stock_data/formatted/goog_5y.csv"
    test_prediction_data_path = "C:/Users/God/data/stock_data/formatted/google_pred_test_ytd.csv"
    save_path = "C:/Users/God/data/stock_data/goog_5y_price_preds.csv"

    # Data for training/testing the model
    data_df = pd.read_csv(data_path)
    X, y = format_X_y(data_df)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2)

    # Validation data with actual data taken from a time period outside of the train/test data.
    prediction_df = pd.read_csv(test_prediction_data_path)
    X_valid, y_true = format_X_y(prediction_df)

    # Iterates over all regression models in the dictionary.
    for regressor in regressor_dict.keys():

        clf = regressor_dict[regressor]

        print(f"Training model: {regressor}")
        clf.fit(X, y)

        y_pred = clf.predict(X_valid)
        print(f"Prediction: {y_pred}\n"
              f"    Actual: {y_true}")

        get_metrics(clf, X_test, y_test, name=regressor)


if __name__ == '__main__':
    main()