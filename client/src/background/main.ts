import {Storage} from '@plasmohq/storage';
import {StorageConstants} from '~src/constants/StorageConstants';
import type {SettingsModel} from '~src/models/AccountModel';
import {fetchCode, setValueOnWebpage} from '~src/background/utils';

export const initAccountMenuItems = (): void => {
    chrome.runtime.onInstalled.addListener(async () => {
        addAccountsMenuItems();
    });
};

export const reloadAccountMenuItems = async (): Promise<void> => {
    chrome.contextMenus.removeAll();
    addAccountsMenuItems();
};

export const addAccountsMenuItems = (): void => {
    const storage = new Storage();
    storage.get<SettingsModel>(StorageConstants.SETTINGS).then((configuration) => {
        configuration?.accounts?.forEach((account, idx) => {
            chrome.contextMenus.create({
                id: String(idx),
                title: account.name,
                type: 'normal',
                contexts: ['editable'],
            });
        });
    });
};
//
export const addOnClickForMenuItems = (): void => {
    chrome.contextMenus.onClicked.addListener(async (item, tab) => {
        if (!tab) return;

        await setValueByAccountId(String(item.menuItemId), tab.id);
    });
};

const setValueByAccountId = async (accountIdx: string, tabId: number): Promise<void> => {
    const storage = new Storage();
    const settings = await storage.get<SettingsModel>(StorageConstants.SETTINGS);
    const account = settings.accounts[Number(accountIdx)];
    const code = await fetchCode(settings.url, account);
    await chrome.scripting.executeScript({
        target: {
            tabId,
        },
        world: 'MAIN',
        func: setValueOnWebpage,
        args: [code],
    });
};
