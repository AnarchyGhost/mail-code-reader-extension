import {type PlasmoMessaging, sendToBackground} from '@plasmohq/messaging';
import {reloadAccountMenuItems} from '~src/background/main';

const handler: PlasmoMessaging.MessageHandler = async (req, res) => {
    await reloadAccountMenuItems();
    res.send({
        message: 'OK'
    });
};

export default handler;

export const sendUpdateAccountsMessage = async () => {
    await sendToBackground({
        name: 'updateAccounts',
        body: {}
    });

};