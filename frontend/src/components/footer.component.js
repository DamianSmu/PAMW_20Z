import React, { Component } from 'react'

class FooterComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {

        }
    }

    render() {
        return (
            <footer className="footer">
                <div className="row">
                    <div className="col-sm-5">
                        <p>© 2020 Damian Smugorzewski</p>
                    </div>
                    <div className="col-sm-5">
                        <a className="link" href="https://github.com/DamianSmu/PAMW_20Z">Repozytorium</a>
                    </div>
                </div>
            </footer>
        )
    }
}

export default FooterComponent