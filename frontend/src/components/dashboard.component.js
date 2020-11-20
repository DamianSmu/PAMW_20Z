import React, {Component} from "react";

export default class Dashboard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            content: ""
        };
    }

    render() {
        return (
                <div className="jumbotron">
                    <h1 className="display-3">Paczkopol</h1>
                    <p className="lead">Paczkopol to ciekawy system do obsługi paczek.</p>
                    <p><a className="btn btn-lg btn-success" href="sign-up.html" role="button">Zarejestruj się za
                        darmo!</a>
                    </p>
                </div>
        );
    }
}