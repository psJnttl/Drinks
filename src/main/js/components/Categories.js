import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon, Table} from 'react-bootstrap';

class Categories extends React.Component {
  constructor(props) {
    super(props);
    this.state = {categories: [], infoModalVisible: false, addModalVisible: false,}
    this.fetchCategories = this.fetchCategories.bind(this);
    this.setCategoryList = this.setCategoryList.bind(this);
  }

  fetchCategories() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/categories', config)
         .then(function (response) {
            self.setCategoryList(response.data);
         })
         .catch(function (response) {
            self.setState({infoModalVisible: true});
         });
  }

  setCategoryList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({categories: theList});
  }

  componentDidMount() {
    this.fetchCategories();
  }

  render() {
    const dataRows = this.state.categories.map( (row, index) =>
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.name}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.setDeleteConfirmModalVisible(row)} title="delete"><Glyphicon glyph="trash"/></Button>
          <Button bsStyle="warning" bsSize="small" onClick={() => this.openEditModal(row)} title="edit"><Glyphicon glyph="pencil"/></Button>
        </td>
      </tr>
    );

    return (
      <div>

        <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add"><Glyphicon glyph="plus"/></Button>
        
        <Table bordered condensed hover>
          <thead>
            <tr>
              <th>id</th>
              <th>name</th>
              <th>action</th>
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>
      </div>
    );
  }
}
Categories.PropTypes = {}
Categories.defaultProps = {}
export default Categories;
