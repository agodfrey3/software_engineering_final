import json
import matplotlib.pyplot as plt

from sklearn.metrics import mean_squared_error


def plot_results(X_test, y_test, y_pred):
    """
    Plots the data points against the resulting model.
    :param X_test: X_test data.
    :param y_test: Actual y values.
    :param y_pred: Model's predicted y values.
    """
    plt.scatter(X_test, y_test, color='black')
    plt.plot(X_test, y_pred, color='blue', linewidth=3)

    plt.xticks(())
    plt.yticks(())

    plt.show()


def get_metrics(clf, X_test, y_test, print_results: bool=True, name: str="unspecified_model"):
    """
    Outputs or returns the metrics for a given regression model.
    :param clf: Regression model.
    :param X_test: X_test data.
    :param y_test: y_test data.
    :param print_results: Whether or not metrics should be printed.
    :param name: Name of the model for which metrics are being calculated.
    :return: Metrics dict iff print_results is False.
    """
    # Get predictions on test set.
    y_pred = clf.predict(X_test)

    # Calculate scores/errors.
    confidence = clf.score(X_test, y_test)
    mean_sq = float(mean_squared_error(y_test, y_pred))

    metrics = {
        'name': name,
        'r2 score': confidence,
        'mean_squared': mean_sq,
    }

    if print_results:
        print(json.dumps(metrics, indent=4))
    else:
        return metrics