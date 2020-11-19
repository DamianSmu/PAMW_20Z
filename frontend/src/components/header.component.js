import React, { Component } from 'react'

class HeaderComponent extends Component {
    constructor(props) {
        super(props)

        this.state = {
        }
    }

    render() {
        return (
            <header className="header clearfix">
                <nav>
                    <ul className="nav nav-pills float-right">
                        <li className="nav-item">
                            <a className="nav-link active" href="/">Strona główna
                                </a>
                        </li>
                        <li className="nav-item">
                            <a className="nav-link" href="/signup">Rejestracja</a>
                        </li>
                    </ul>
                </nav>
                <h3 className="text-muted">PaczkoPol</h3>
            </header>
        )
    }
}

export default HeaderComponent