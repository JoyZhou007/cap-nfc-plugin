import { OptionsRequiredError } from './errors';
import { WebPlugin } from '@capacitor/core';
import { NFCPluginPlugin, TaskInfo } from './definitions';

export class NFCPluginWeb extends WebPlugin implements NFCPluginPlugin {
  constructor() {
    super({
      name: 'NFCPlugin',
      platforms: ['web']
    });
  }
  async sendTaskInfo(options: TaskInfo): Promise<TaskInfo> {
    if(!options){
      return Promise.reject(new OptionsRequiredError());
    }
    return Promise.resolve(options);
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
