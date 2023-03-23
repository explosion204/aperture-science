import PropTypes from 'prop-types';
import Item from "./Item";

const Items = ({ items, onDelete, onUpdate }) => {
    return (
        items.map(i => <Item key={i.id} item={i} onDelete={onDelete} onUpdate={onUpdate}/>)
    );
};

Items.propTypes = {
    items: PropTypes.array,
    onDelete: PropTypes.func,
    onMark: PropTypes.func
};

export default Items;