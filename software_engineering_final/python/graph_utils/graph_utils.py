import pandas as pd
import matplotlib.pyplot as plt
from matplotlib import pyplot as plt
import numpy as np


def format_df_for_graph(df):
    rows = []
    for i, row in df.iterrows():
        row['index'] = i
        rows.append(row)

    return pd.DataFrame(rows)


def main():

    tickers = ['aapl', 'ba', 'fb', 'goog', 'msft']

    for ticker in tickers:
        data_path = f"C:/Users/God/data/stock_data/{ticker}_5y.csv"

        df = pd.read_csv(data_path)
        df = format_df_for_graph(df)

        dfs = np.array_split(df, 10)

        for df in dfs:
            x = [i for i in range(len(df))]
            y = np.array(df['price'].tolist())

            save_name = f"{y[-1]}_{y[-2]}_{ticker}.png"
            x = x[:-1]
            y = y[:-1]

            title = ticker.upper()
            plt.title(title, fontsize=18)
            plt.xlabel('Day Number', fontsize=18)
            plt.ylabel('Price', fontsize=18)
            plt.plot(x, y)
            plt.savefig(save_name)
            plt.clf()



if __name__ == '__main__':
    main()