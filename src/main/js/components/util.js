import _ from 'lodash';

export function concatenateSearchResults(target, source) {
  const result1 = source.filter( item => !checkDuplicate(target, item));
  const result = _.concat(target, result1);
  return result;
}

export function checkDuplicate(list, item) {
  const index = _.findIndex(list, item);
  if (-1 === index) {
    return false;
  }
  return true;
}
