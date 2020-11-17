import React, {Component} from "react";

export default class StartComponent extends Component {
    constructor(props) {
        super(props);
        this.onChangeTitle = this.onChangeTitle.bind(this);
        this.onChangeDescription = this.onChangeDescription.bind(this);


        this.state = {
            id: null,
            title: "",
            description: "",
            published: false,

            submitted: false
        };
    }




    render() {
        return (
            <div className="jumbotron">
                <h1 className="display-3">Paczkopol</h1>
                <p className="lead">Paczkopol to ciekawy system do obsługi paczek.</p>
                <p><a className="btn btn-lg btn-success" href="sign-up.html" role="button">Zarejestruj się za darmo!</a>
                </p>
            </div>


        );
    }

}
