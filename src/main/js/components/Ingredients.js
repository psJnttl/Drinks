import React from 'react';
import PropTypes from 'prop-types';
import SimpleInformationModal from './SimpleInformationModal';
import axios from 'axios';
import _ from 'lodash';
import {Button, Pagination, Table} from 'react-bootstrap';

class Ingredients extends React.Component {
  constructor(props) {
    super(props);
    this.state = {ingredients: [], infoModalOpen: false, }
    this.setIngredientList = this.setIngredientList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
  }

  setIngredientList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({ingredients: theList});
  }
  closeInfoModal() {
    this.setState({infoModalOpen: false});
  }
  componentDidMount() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/ingredients', config)
         .then(function (response) {
              self.setIngredientList(response.data);
         })
        .catch(function (response) {
          this.setState({infoModalOpen: true});
        });
  }
  render() {
    const dataRows = this.state.ingredients.map( (item, index) =>
      <tr>
        <td>{item.id}</td>
        <td>{item.name}</td>
      </tr>
    );
    return (
      <div>
        <SimpleInformationModal
          modalOpen={this.state.infoModalOpen}
          header="failedModalHeader"
          title="Fetch ingredients failed"
          notification = "Could not get the list of ingredients from server!"
          notification2 = {this.state.notification2}
          name=""
          reply={this.closeInfoModal} />
        <Table bordered condensed hover>
          <thead>
            <tr>
              <th>id</th>
              <th>name</th>
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
Ingredients.PropTypes = {}
Ingredients.defaultProps = {}
export default Ingredients;
