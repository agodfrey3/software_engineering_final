import pandas as pd
from ast import literal_eval
import numpy as np
import os
from typing import List


def format_X_y(df: pd.DataFrame, X_col: str='X', y_col: str='y'):
    """
    Converts input and output data into a format usable by SKLearn.
    :param df: DataFrame containing input and output data.
    :param X_col: Name of the column which holds input data.
    :param y_col: Name of the column which holds outputs data.
    :return: Input and output numpy arrays for use with SKLearn regression models.
    """
    X = time_series_to_X(df=df, X_col=X_col)
    y = np.array(df[y_col])

    return X, y


def time_series_to_X(df: pd.DataFrame, X_col: str='X'):
    """
    Converts input data into a format usable with SKLearn.
    :param df: DataFrame containing the X values to be processed.
    :param X_col: Name of the column in which the input data is located.
    :return: numpy.array of X values.
    """
    df['X'] = df['X'].apply(lambda x: literal_eval(x))
    return np.asarray(df['X'].tolist())


def split_train_test(df: pd.DataFrame, percent: float=0.8):
    """
    Creates a train and test set by random sampling where 'percent' of the initial
    data is used for training.
    :param df: The DataFrame to split.
    :param percent: The percentage of data to use for training.
    :return: A DataFrame consisting of train data, and a DataFrame consisting of test/validation data.
    """
    df = df.sample(frac=1).reset_index(drop=True)
    num_rows = len(df)

    split_index = int(percent * num_rows)

    train_df = df.iloc[:split_index]
    test_df = df.iloc[split_index + 1:]

    return train_df, test_df


def split_time_series(df: pd.DataFrame, chunk_size: int, target_col: float='price'):
    """
    Splits time-series data in chunks of chunk_size, where the last index is y, and the rest
    of the data is X.
    :param df: DataFrame containing the time-series data.
    :param chunk_size: Size of arrays to be created for training/testing.
    :param target_col: The column name of the data to be used.
    :return: DataFrame consisting of two columns: X and y.
    """
    df_size = len(df)
    rows = []
    for i in range(df_size - chunk_size - 1):
        row = {'X': df.iloc[i:i+chunk_size-1][target_col].values.tolist(), 'y': df.iloc[i+chunk_size][target_col]}
        rows.append(row)
    return pd.DataFrame(rows)


def combine_data_from_dir(save_name: str, search_dir: str, save_dir: str, chunk_sizes: List[int]=[5]):

    master_df = pd.DataFrame()

    for chunk_size in chunk_sizes:
        for filename in os.listdir(search_dir):
            if filename.endswith(".csv"):
                print(f"Formatting {filename}")

                df = pd.read_csv(f"{search_dir}/{filename}", encoding='utf-8')

                df = split_time_series(df, chunk_size=10)

                master_df = master_df.append(df)

        print(f"Saving df to {save_dir}/{save_name}_chunk_{chunk_size}.csv")
        master_df.to_csv(f"{save_dir}/{save_name}_chunk_{chunk_size}.csv", index=False, encoding='utf-8')

def main():
    main_dir = "C:/Users/Andrew/data/stock_data/"
    save_dir = "C:/Users/Andrew/data/stock_data/formatted/validation"

    validation_dir = os.path.join(main_dir, "validation")

    combine_data_from_dir("justin_validation", search_dir=validation_dir, save_dir=save_dir)


if __name__ == '__main__':
    main()
