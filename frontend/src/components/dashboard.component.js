import React, { Component } from "react";
import parcelService from "../services/parcel.service";
import _ from 'lodash';


export default class Dashboard extends Component {
    constructor(props) {
        super(props);

        this.state = {
            parcelList: ""

        };
    }

    renderParcel(id, receiver, postOffice, size) {
        if (!id) return <div />;
        return (
            <div className="card card-outline-danger">
                <div className="card-header container-fluid">
                    <div className="row">
                        <div className="col-md-8">
                            <h5>Id: {id}</h5>
                        </div>
                        <div className="col">
                            <button type="button" className="close" aria-label="Close" onClick={this.deleteParcel(id)}>
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </div>
                </div>
                <div className="card-body">
                    <h6>Odbiorca: {receiver}</h6>
                    <h6>Skrytka: {postOffice}</h6>
                    <p>Rozmiar: {size}</p>
                </div>
            </div>
        );
    };

    componentDidMount() {
        parcelService.getAll().then(response => {
            this.setState({
                parcelList: response.parcelList
            });
        });
    }

    deleteParcel(id) {
        // parcelService.delete(id).then(response => {
        //     this.setState({

        //     })
        // });
        console.log(id)
    }

    renderParcels() {
        return _.map(this.state.parcelList, key => {
            return (
                <div key={key}>
                    {this.renderParcel(
                        key.id,
                        key.receiver,
                        key.postOffice,
                        key.size,
                    )}
                </div>


            );
        });
    }

    render() {
        return (
            <div className="container content">
                <h3>Twoje przesyłki:</h3>
                {this.renderParcels()}
            </div>

        )

    }
}