import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Glyphicon, Pagination, Table} from 'react-bootstrap';

class Pages extends React.Component {
  constructor(props) {
    super(props);
    this.state = {currentPage: 1, itemsPerPage: 10,}
    this.setCurrentPage = this.setCurrentPage.bind(this);
    this.setItemsPerPage = this.setItemsPerPage.bind(this);
    this.paginate = this.paginate.bind(this);
  }

  setCurrentPage(pageNbr) {
    this.setState({currentPage: pageNbr})
  }

  setItemsPerPage(nbrItems) {
    this.setState({itemsPerPage: nbrItems, currentPage: 1});
  }

  paginate (item, index) {
    return (index >= (this.state.currentPage-1) * this.state.itemsPerPage) &&
      (index < (this.state.currentPage-1) * this.state.itemsPerPage + this.state.itemsPerPage);
  }

  componentWillReceiveProps(newProps) {
    const oldLen = this.props.items.length;
    const newLen = newProps.items.length;
    if (oldLen !== newLen) {
      this.setState({currentPage: 1});
    }
  }

  render() {
    const pageAmount = Math.ceil(this.props.items.length / this.state.itemsPerPage);
    const itemsOnPage = this.props.items.filter ( (item, index) => this.paginate(item, index) );

    const header = this.props.itemToList.map((item, index) =>
      <th key={index}>{item}</th> );
    const dataRows = itemsOnPage.map( (row, index) => this.props.dataTool(row, index));
    return (
      <div>
        <Table bordered condensed hover>
          <thead>
            <tr>
              {header}
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>
        <Pagination
          bsSize="medium"
          items={ pageAmount }
          activePage={this.state.currentPage}
          onSelect={ this.setCurrentPage}
          prev
          next
          boundaryLinks
          ellipsis
          maxButtons={5}
        /> <br/>
        <Button bsStyle={this.state.itemsPerPage === 5 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(5)}>5</Button>
        <Button bsStyle={this.state.itemsPerPage === 10 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(10)}>10</Button>
        <Button bsStyle={this.state.itemsPerPage === 20 ? "primary" : "default"} bsSize="small" onClick={() => this.setItemsPerPage(20)}>20</Button>
      </div>
    );
  }
}
Pages.PropTypes = {
  items: PropTypes.array.isRequired,
  itemToList: PropTypes.array.isRequired,
  dataTool: PropTypes.func.isRequired,
}
Pages.defaultProps = {}
export default Pages;
