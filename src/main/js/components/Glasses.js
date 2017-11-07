import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import {Button, Glyphicon, Table} from 'react-bootstrap';
import IngredientModal from './IngredientModal';

class Glasses extends React.Component {
  constructor(props) {
    super(props);
    this.state = {glasses: [], infoModalVisible: false, addModalVisible: false,
        }
    this.fetchGlasses = this.fetchGlasses.bind(this);
    this.setGlassList = this.setGlassList.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.addGlass = this.addGlass.bind(this);
  }

  fetchGlasses() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/glasses', config)
         .then(function (response) {
              self.setGlassList(response.data);
         })
        .catch(function (response) {
          this.setState({infoModalVisible: true});
        });
  }

  setGlassList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, name: item.name}) );
    this.setState({glasses: theList});
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false});
  }

  openAddModal() {
    this.setState({addModalVisible: true});
  }

  closeAddModal() {
    this.setState({addModalVisible: false});
  }

  componentDidMount() {
    this.fetchGlasses();
  }

  render() {
    let addModal;
    if (true === this.state.addModalVisible) {
      addModal =
      <IngredientModal
        modalOpen={this.state.addModalVisible}
        title="Add glass"
        close={this.closeAddModal}
        save={this.addGlass}
        placeholder="glass name"
      />
    }
    else {
      addModal = null;
    }
    const dataRows = this.state.glasses.map( (row, index) =>
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

        {addModal}
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

  addGlass(glass) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const command  =  _.assign({}, glass);
    axios.post('api/glasses', command, config)
         .then(function (response) {
              console.log("add glass ok");
              const glsss = _.concat(self.state.glasses, response.data);
              self.setState({glasses: glsss});
         })
        .catch(function (response) {
          console.log("add glass failed");
        });
  }


}
Glasses.PropTypes = {}
Glasses.defaultProps = {}
export default Glasses;
