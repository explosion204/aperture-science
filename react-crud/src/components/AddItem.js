import { useState } from 'react';
import PropTypes from 'prop-types';

const AddItem = ({onAdd}) => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    
    const onInputChange = (e, mutator) => {
        mutator(e.target.value);
    };

    const onSubmit = e => {
        e.preventDefault();

        if (!title || !description) {
            return;
        }

        onAdd({ title, description, marked: false });
        setTitle('');
        setDescription('');
    };

    return (
        <form className="d-flex flex-column w-100" 
              onSubmit={onSubmit}>

            <input type="text" 
                   className="form-control mt-1" 
                   placeholder="Title" 
                   value={title}
                   onChange={e => onInputChange(e, setTitle)}/>

            <input type="text" 
                   className="form-control mt-3" 
                   placeholder="Description" 
                   value={description}
                   onChange={e => onInputChange(e, setDescription)}/>

            <input type="submit" 
                   className="btn mt-3 mb-1" 
                   style={{ backgroundColor: "green", color: "white" }} 
                   value="Save"/>
        </form>
    )
};

AddItem.propTypes = {
    onAdd: PropTypes.func
};

export default AddItem;
