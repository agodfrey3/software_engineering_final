import pickle
from os import listdir
from os.path import isfile, join


def get_files_in_dir(dir: str):
    """
    Returns a list of the file's full filespaths from the passed directory.
    :param dir: The directory to search through.
    :return: A list of filenames.
    """

    file_list = [f"{dir}/{file}" for file in listdir(dir) if isfile(join(dir, file))]

    return file_list


def save_pkl_file(o, p):
    """
    Saves .pkl file.
    :param o: Object to be saved.
    :param p: Path to save the object to.
    """
    output = open(p, 'wb')
    pickle.dump(o, output)
    output.close()


def load_pkl_file(p):
    """
    Loads a .pkl file.
    :param p: Path to load .pkl file from.
    :return: The loaded python object stored at the passed path.
    """
    pkl_file = open(p, 'rb')
    obj = pickle.load(pkl_file)
    pkl_file.close()
    return obj


def save_train_test(train_df, test_df, save_path: str, save_as_dict: bool=True):
    """
    Saves train and test DataFrames.
    :param train_df: DataFrame holding training data.
    :param test_df: DataFrame holding testing data.
    :param save_path: Path to save the train/test data to.
    :param save_as_dict: Whether or not to save the train/test DataFrames together in a dict.
    """
    if save_as_dict:
        data = {
            'train_df': train_df,
            'test_df': test_df
        }
        save_pkl_file(data, save_path)
    else:
        # Remove the extension so we can add 'train' or 'test' to the save_path.
        save_path = save_path[:-4]
        save_pkl_file(train_df, f"{save_path}_train.csv")
        save_pkl_file(test_df, f"{save_path}_test.csv")