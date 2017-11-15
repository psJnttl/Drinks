import React from 'react';
import PropTypes from 'prop-types';
import {FormControl, FormGroup, SplitButton, MenuItem} from 'react-bootstrap';
import _ from 'lodash';

class SelectEntity extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    const title = <option key={-1} value={{}}>{this.props.title}</option>
    const optionList1 = this.props.entityList.map( (item, i) => (
      this.props.value.id === item.id ?
        <option key={i} value={item.id} selected>{item.name}</option>
      :
        <option key={i} value={item.id}>{item.name}</option>
    ));
    const optionList = _.concat(title, optionList1);
    return (
      <div>
        <FormControl componentClass="select" onChange={(e) => {this.props.onSelect(e)}}>
          {optionList}
        </FormControl>
      </div>
    );
  }
}
SelectEntity.PropTypes = {
  title: PropTypes.string,
  entityList: PropTypes.array.isRequired,
  onSelect: PropTypes.func.isRequired,
  value: PropTypes.object.isRequired,
}
SelectEntity.defaultProps = {
  title: "Choose from",
}
export default SelectEntity;
