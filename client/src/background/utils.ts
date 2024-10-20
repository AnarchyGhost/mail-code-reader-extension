import type {AccountModel} from '~src/models/AccountModel';
import type {CodeResponseModel} from '~src/models/CodeResponseModel';

export const setValueOnWebpage = (value: string): void => {
    const element = <HTMLInputElement>document.activeElement;
    const event = new ClipboardEvent('paste', {clipboardData: new DataTransfer()});
    event.clipboardData.setData('text', value);
    element.dispatchEvent(event);
};

export const fetchCode = async (url: string, account: AccountModel): Promise<string> => {
    const response = await fetch(url, {
        method: 'POST',
        body: JSON.stringify((
            {
                ...account,
                name: undefined
            })),
        headers: {
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) return '';
    const result = await response.json() as CodeResponseModel;
    return result.code;
};
