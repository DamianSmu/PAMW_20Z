import React, { Component } from "react";
import parcelService from "../services/parcel.service";
import _ from 'lodash';

const Parcel = ({ id, receiver, postOffice, size }) => {
    if (!id) return <div />;
    return (
        <div className="card">
            <div className="card-body">
                <h5>Id: {id}</h5>
                <h6>Odbiorca: {receiver}</h6>
                <h6>Skrytka: {postOffice}</h6>
                <p>Rozmiar: {size}</p>
            </div>
        </div>
    );
};

export default class Dashboard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            parcelList: ""

        };
    }

    componentDidMount() {
        parcelService.getAll().then(response => {
            this.setState({
                parcelList: response.parcelList
            });
        });
    }


    renderParcels() {
        return _.map(this.state.parcelList, key => {
            return (
                
                    <Parcel
                        id={key.id}
                        receiver={key.receiver}
                        postOffice={key.postOffice}
                        size={key.size}
                    />
                
            );
        });
    }

    render() {
        return (
            <div className="container content">
                {this.renderParcels()}
            </div>

        )

    }
}