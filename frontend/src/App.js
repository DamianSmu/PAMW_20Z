import React, { Component } from "react";
import { Switch, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import Signup from "./components/signup.component";
import Register from "./components/register.component";
import Dashboard from "./components/dashboard.component";
import Start from "./components/start.component";

class App extends Component {
  render() {
    return (
        <div className="container">
          <div className="header clearfix">
            <nav>
              <ul className="nav nav-pills float-right">
                <li className="nav-item">
                  <Link to={"/signup"} className="nav-link">
                    Zaloguj
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to={"/register"} className="nav-link">
                    Zarejestruj
                  </Link>                </li>
              </ul>
            </nav>
            <h3 className="text-muted">PaczkoPol</h3>
          </div>

          <div className="container mt-3">
            <Switch>
              <Route exact path="/" component={Start} />
              <Route exact path="/signup" component={Signup} />
              <Route exact path="/register" component={Register} />
              {/*<Route path="/tutorials/:id" component={Tutorial} />*/}
            </Switch>
          </div>
        </div>

          );
  }
}


export default App;