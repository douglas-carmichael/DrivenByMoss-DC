package de.mossgrabers.controller.mackie.hui.command.trigger;

import de.mossgrabers.controller.mackie.hui.HUIConfiguration;
import de.mossgrabers.controller.mackie.hui.controller.HUIControlSurface;
import de.mossgrabers.controller.mackie.hui.controller.HUIDisplay;
import de.mossgrabers.framework.command.trigger.transport.AutomationModeCommand;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.constants.AutomationMode;
import de.mossgrabers.framework.utils.ButtonEvent;

public class HUIAutomationModeCommand extends AutomationModeCommand<HUIControlSurface, HUIConfiguration>
{
    private final HUIDisplay display;
    private final AutomationMode ourAutomationMode;

    public HUIAutomationModeCommand(final AutomationMode autoMode, final IModel model, final HUIControlSurface surface, final HUIDisplay display)
    {
        super(autoMode, model, surface);
        this.ourAutomationMode = autoMode;
        this.display = display;
    }

    private void illuminateLEDs(int ledState)
    {
        int channel = 0;
        int[] huiControlArray = {HUIControlSurface.HUI_SELECT1, HUIControlSurface.HUI_SELECT2, HUIControlSurface.HUI_SELECT3,
                HUIControlSurface.HUI_SELECT4, HUIControlSurface.HUI_SELECT5, HUIControlSurface.HUI_SELECT6,
                HUIControlSurface.HUI_SELECT7, HUIControlSurface.HUI_SELECT8};
        for (int i = 0; i < huiControlArray.length; i++) {
            int huiControl = huiControlArray[i] + channel * 8;
            this.surface.setTrigger(0, huiControl, ledState);
        }
    }

    @Override
    public void executeNormal(final ButtonEvent event)
    {
        super.executeNormal(event);

            final AutomationMode mode = this.model.getTransport().getAutomationWriteMode();
            if (mode == this.ourAutomationMode) {
                if (this.surface.getConfiguration().shouldNotifyAutomationMode()) {
                    this.illuminateLEDs(127);
                    this.display.notifyHUIDisplay("Automation: " + mode.name());
                    this.illuminateLEDs(0);
                }
            }
    }
}