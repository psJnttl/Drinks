import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {drinks: [], infoModalVisible: false,  };
    this.fetchDrinks = this.fetchDrinks.bind(this);
    this.setDrinkList = this.setDrinkList.bind(this);
  }

  fetchDrinks() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/drinks', config)
         .then(function (response) {
            self.setDrinkList(response.data);
         })
         .catch(function (response) {
            self.setState({infoModalVisible: true});
         });
  }

  setDrinkList(data) {
    const theList = data.map( item =>
       _.assign({}, {
         id: item.id,
         name: item.name,
         category: item.category,
         glass: item.glass,
         components: item.components
       }) );
    this.setState({drinks: theList});
  }

  componentDidMount() {
    this.fetchDrinks();
  }
  render() {
    return (
      <h4>Drinks listed here</h4>
    );
  }
}
Drinks.PropTypes = {}
Drinks.defaultProps = {}
export default Drinks;
