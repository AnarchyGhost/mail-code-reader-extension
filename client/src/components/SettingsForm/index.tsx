import {useStorage} from '~node_modules/@plasmohq/storage/dist/hook';
import {StorageConstants} from '~src/constants/StorageConstants';
import React, {useCallback, useState} from 'react';
import type {SettingsModel} from '~src/models/AccountModel';
import {Button, Flex, Typography} from 'antd';
import TextArea from '~node_modules/antd/es/input/TextArea';
import {sendUpdateAccountsMessage} from '~src/background/messages/updateAccounts';

const {Title, Text} = Typography;

function SettingsForm() {
    const [settingsStorage, setSettingsStorage] =
        useStorage<SettingsModel>(StorageConstants.SETTINGS);
    const [settings, setSettings] = useState(null);
    const [error, setError] = useState(null);

    const updateSettings = useCallback(
        (event: { target: { value: string } }) => {
            setSettings(event.target.value);
        }, [],
    );

    const saveSettings = useCallback(() => {
        setError(null);
        try {
            const json = JSON.parse(settings);
            setSettingsStorage(json).then(() =>
                console.log('saved settings'),
            );
            sendUpdateAccountsMessage().then();
        } catch (e) {
            setError('Ошибка при обработке JSON');
        }

    }, [settings]);

    const cancelSettings = useCallback(() => {
        setError(null);
        setSettings(JSON.stringify(settingsStorage, null, 2));
    }, [settingsStorage]);

    if (settings == null && settingsStorage) {
        setSettings(JSON.stringify(settingsStorage, null, 2));
    }

    const isDisabledSaveButton =
        !settings || JSON.stringify(settingsStorage, null, 2) == settings;

    return (
        <Flex gap="middle" vertical>
            <Title level={5}>Settings</Title>
            <Text type='danger'>{error}</Text>
            <TextArea
                title='JSON Config'
                value={settings}
                onChange={updateSettings}
                rows={14}
                placeholder="Пароль"
            />
            <Flex gap="large">
                {!isDisabledSaveButton && (
                    <Button type="dashed" onClick={cancelSettings} block>
                        Cancel
                    </Button>
                )}
                <Button
                    type="primary"
                    onClick={saveSettings}
                    disabled={isDisabledSaveButton}
                    block>
                    Save
                </Button>
            </Flex>
        </Flex>
    );
}

export default SettingsForm;