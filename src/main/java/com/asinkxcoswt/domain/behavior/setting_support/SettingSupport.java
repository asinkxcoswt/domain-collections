package com.asinkxcoswt.domain.behavior.setting_support;

import com.asinkxcoswt.domain.behavior.DomainBehaviorNotImplementedException;
import com.asinkxcoswt.domain.behavior.DomainBehaviorTarget;

public interface SettingSupport {
    @DomainBehaviorTarget
    default Setting getSettingValue(String key) {
        throw new DomainBehaviorNotImplementedException();
    }
}
