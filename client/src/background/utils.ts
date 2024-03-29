import type {AccountModel} from '~src/models/AccountModel';
import type {CodeResponseModel} from '~src/models/CodeResponseModel';

export const setValueOnWebpage = (value: string): void => {
    const element = <HTMLInputElement>document.activeElement;
    element.value = value;
    element.dispatchEvent(new Event('input', {}));
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
    if (response.ok) {
        const result = await response.json() as CodeResponseModel;
        return result.code;
    } else return '';
};
