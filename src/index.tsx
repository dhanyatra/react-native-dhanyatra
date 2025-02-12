import { NativeEventEmitter, NativeModules } from 'react-native';
import type { PaymentOptions } from './PaymentOption';
export type { PaymentOptions } from './PaymentOption';

const dhanyatraEvents = new NativeEventEmitter(
  NativeModules.DhanyatraEventEmitter
);

const removeSubscriptions = () => {
  dhanyatraEvents.removeAllListeners('Dhanyatra::PAYMENT_SUCCESS');
  dhanyatraEvents.removeAllListeners('Dhanyatra::PAYMENT_ERROR');
};

class DhanyatraCheckout {
  static open(
    options: PaymentOptions,
    successCallback?: any,
    errorCallback?: any
  ) {
    return new Promise(function (resolve, reject) {
      dhanyatraEvents.addListener('Dhanyatra::PAYMENT_SUCCESS', (data) => {
        let resolveFn = successCallback || resolve;
        resolveFn(data);
        removeSubscriptions();
      });
      dhanyatraEvents.addListener('Dhanyatra::PAYMENT_ERROR', (data) => {
        let rejectFn = errorCallback || reject;
        rejectFn(data);
        removeSubscriptions();
      });
      NativeModules.RNDhanyatraCheckout.startPayment(options);
    });
  }
}

export default DhanyatraCheckout;
