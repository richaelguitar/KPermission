
package com.kad.kpermission;


public interface SettingService {

    /**
     * Execute setting.
     */
    void execute();

    /**
     * Execute setting with requestCode.
     */
    void execute(int requestCode);

    /**
     * Cancel the operation.
     */
    void cancel();
}
