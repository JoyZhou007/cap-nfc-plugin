import { WebPlugin } from '@capacitor/core';
import { NFCPluginPlugin } from './definitions';

export class NFCPluginWeb extends WebPlugin implements NFCPluginPlugin {
  constructor() {
    super({
      name: 'NFCPlugin',
      platforms: ['web']
    });
  }
  async sendTaskInfo(options: { taskInfo: Object; }): Promise<Object> {
    console.log('Send Task Info Success',options);
    return options;
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }

  async testEvent(): Promise<void> {
    console.log("listen test event success...");
  }
}

const NFCPlugin = new NFCPluginWeb();

export { NFCPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(NFCPlugin);
