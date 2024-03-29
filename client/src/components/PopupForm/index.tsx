import {Menu, type MenuProps, Typography} from 'antd';
import React, {useState} from 'react';

import SettingsForm from '../SettingsForm';

const {Title} = Typography;

function PopupForm() {
    enum menuItemProps {
        MAIN = 'main',
    }

    const items: MenuProps['items'] = [
        {
            label: 'Main Settings',
            key: menuItemProps.MAIN,
        },
    ];
    const [current, setCurrent] = useState('main');

    const onMenuClicked: MenuProps['onClick'] = (e) => {
        setCurrent(e.key);
    };

    let form: React.JSX.Element;
    switch (current) {
    default:
        form = <SettingsForm/>;
    }


    return (
        <div>
            <Title level={4}>Mail code reader</Title>
            <Menu
                onClick={onMenuClicked}
                selectedKeys={[current]}
                mode="horizontal"
                items={items}
            />
            {form}
        </div>
    );
}

export default PopupForm;
