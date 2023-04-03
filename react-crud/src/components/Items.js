import PropTypes from 'prop-types';
import Item from "./Item";
import { useItemContext } from '../contexts/ItemContext';

const Items = () => {
    const { items } = useItemContext();

    return (
        items.length > 0 ? items.map(i => <Item key={i.id} item={i}/>) : 'Nothing to show'
    );
};

export default Items;