from sklearn.linear_model import LinearRegression, Ridge, RidgeCV, Lasso, BayesianRidge

regressor_dict = {
    'one_means_squared': LinearRegression(),
    'ridge': Ridge(alpha=.5),
    'ridge_cv': RidgeCV(alphas=[0.1, 1.0, 10.0]),
    'lasso': Lasso(alpha=0.1, max_iter=100000),
    'bayesian_ridge': BayesianRidge()
}