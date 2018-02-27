"""
Andrew Godfrey
Primary scrapers and data management for Software Engineering Final project.
Start date: 15 February 2018
"""

import requests
import pandas as pd
import os
from typing import List, Dict


def fetch_stock_data_helper(stock: str, period: str, verbose: bool=False):
    """
    ***NOT INTENDED TO BE CALLED DIRECTLY***
    :param stock: The stock symbol to get data on.
    :param period: The period of time to get data from.
                Note: Must be on of ['5y', '2y', '1y', 'ytd', '6m', '3m', '1m']
    :param verbose: Will print sample of DataFrame for verbose=True.
    :return: A pandas DataFrame consisting of the date and open prices within the passed
             date range for the passed stock.
    """

    response = requests.get(f"https://api.iextrading.com/1.0/stock/{stock}/chart/{period}")

    if response.status_code != 200:
        raise ValueError

    response_json = response.json()

    price_df = pd.DataFrame.from_dict(response_json)
    price_df['price'] = price_df['open']
    price_df = price_df.dropna().reset_index(drop=True)

    price_df = price_df[['date', 'price']].copy()

    if verbose:
        print(f"Sample of resulting DataFrame for stock: {stock}, period: {period}\n")
        print(price_df.iloc[:5])

    return price_df


def fetch_stock_data(stocks: List[str], period: str, verbose: bool=False, save: bool=False, save_dir: str=None):
    """
    Use this function to create DataFrames of information based on a list of stock symbols
    over a certain period of time.
    :param stocks: A list of stock symbols to gather data on.
    :param period: The period of time to get data from.
                   Note: Must be on of ['5y', '2y', '1y', 'ytd', '6m', '3m', '1m']
    :param save: Saves the resulting data to save_dir if True.
                 NOTE: save_dir must not be None.
    :param save_dir: Location to save the data. Only used if save is True.
    :return: A list of DataFrames. Each DataFrame consists of the prices for a single stock
             over the time period passed.
    """

    if save and save_dir is None:
        raise ValueError("ERROR: Cannot set save to True and not specify save_path!")

    if period not in ['5y', '2y', '1y', 'ytd', '6m', '3m', '1m']:
        raise ValueError("Period must be in ['5y', '2y', '1y', 'ytd', '6m', '3m', '1m']")

    generated_dataframes = []

    for stock in stocks:
        try:
            entry_name = f"{stock}_{period}"
            temp_df = fetch_stock_data_helper(stock=stock, period=period, verbose=verbose)
            generated_dataframes.append({entry_name: temp_df})
        except ValueError:
            print(f"ERROR: Error code thrown when processing stock: {stock} for period: {period}.")

    if save:
        save_resulting_data(generated_dataframes, save_dir=save_dir)

    return generated_dataframes


def save_resulting_data(dataframes: Dict[str, pd.DataFrame], save_dir: str):
    """
    :param dataframes: Dict in the shape of {name: DataFrame} which resulted from fetch_stock_data().
    :param save_dir: The directory to save the DataFrame(s) to.
    """
    for name_df_dict in dataframes:
        for name, df in name_df_dict.items():
            save_path = os.path.join(save_dir, f"{name}.csv")
            df.to_csv(save_path, index=False, encoding='utf-8')


if __name__ == '__main__':
    save_dir = "C:/Users/Andrew/data/stock_data"
    validation_save_dir = os.path.join(save_dir, "validation")
    training_save_dir = os.path.join(save_dir, "training")

    dow_30 = ['axp', 'aapl', 'cat', 'csco', 'cvx', 'xom', 'ge', 'gs', 'hd', 'ibm', 'intc', 'jnj', 'ko', 'jpm', 'mcd',
              'mmm', 'mrk', 'msft', 'nke', 'pfe', 'pg', 'trv', 'unh', 'utx', 'vz', 'v', 'wmt', 'dis', 'dwdp']

    justin_validation = ['jnug', 'jdst', 'uwti', 'dwti']

    df = fetch_stock_data(justin_validation, '5y', save=True, save_dir=validation_save_dir)