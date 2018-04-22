import pandas as pd
import plotly.plotly as py
import plotly.graph_objs as go
import matplotlib.pyplot as plt
import numpy as np

from datetime import datetime

def create_graph_from_df(df):
    data = [go.Scatter(x=df['date'], y=df['price'])]
    py.iplot(data, filename="test_plot")


def format_df_for_graph(df):
    rows = []
    for i, row in df.iterrows():
        row['index'] = i
        rows.append(row)

    return pd.DataFrame(rows)


def main():

    data_path = "C:/Users/Andrew/IdeaProjects/software_engineering_final/" \
                "software_engineering_final/python/data/aapl_5y.csv"

    df = pd.read_csv(data_path)
    df = format_df_for_graph(df)

    for i in range(0, int(len(df) - int(len(df)/10)), int(len(df)/10)):
        temp_df = df.iloc[i:i+int(len(df)/10)]
        print(f"i: {i}, i+mod: {i+int(len(df)/10)}")
        print(f"len: {len(temp_df)}")
        x = np.array(temp_df['index'].tolist())
        y = np.array(temp_df['price'].tolist())
        plt.plot(x, y)
        plt.savefig(f"aapl_{i}.png")

if __name__ == '__main__':
    main()