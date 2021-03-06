declare module "@capacitor/core" {
  interface PluginRegistry {
    NFCPlugin: NFCPluginPlugin;
  }
}

export interface NFCPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  testEvent(): Promise<void>;
  sendTaskInfo(options: TaskInfo): Promise<TaskInfo>;
}

export interface TaskInfo {
  taskInfo: Object;
}
