package com.telenav.kivakit.ui.swing.theme.darcula;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.telenav.kivakit.ui.swing.graphics.style.Color;
import com.telenav.kivakit.ui.swing.graphics.style.Style;
import com.telenav.kivakit.ui.swing.theme.KivaKitTheme;

import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.AQUA;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.BLACK_OLIVE;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.BLUE_RIDGE_MOUNTAINS;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.CHARCOAL;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.FOSSIL;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.GAINSBORO;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.IRON;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.KIVAKIT_YELLOW;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.OCEAN;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.PEWTER;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.PLATINUM;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.SMOKE;
import static com.telenav.kivakit.ui.swing.theme.KivaKitColors.WHITE_SMOKE;

/**
 * @author jonathanl (shibo)
 */
public class KivaKitDarculaTheme extends KivaKitTheme
{
    private static final Color TEXT = GAINSBORO;

    private static final Color FADED_TEXT = PEWTER;

    private static final Color DARK_BACKGROUND = BLACK_OLIVE;

    private static final Color MEDIUM_BACKGROUND = IRON;

    private static final Color HIGHLIGHT = KIVAKIT_YELLOW;

    public KivaKitDarculaTheme()
    {
        FlatDarculaLaf.install();
    }

    @Override
    public Color colorBorder()
    {
        return FOSSIL;
    }

    @Override
    public Color colorCaret()
    {
        return HIGHLIGHT;
    }

    @Override
    public Color colorPanel()
    {
        return CHARCOAL;
    }

    @Override
    public Color colorSeparator()
    {
        return colorBorder();
    }

    @Override
    public Color colorSubPanel()
    {
        return MEDIUM_BACKGROUND;
    }

    @Override
    public boolean isDark()
    {
        return true;
    }

    @Override
    public Style styleButton()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleCaption()
    {
        return styleTitle().withTextFont(fontNormal().deriveFont(11.0f));
    }

    @Override
    public Style styleComponentLabel()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withDrawColor(SMOKE);
    }

    @Override
    public Style styleDropdown()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleInformationLabel()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withDrawColor(BLUE_RIDGE_MOUNTAINS);
    }

    @Override
    public Style styleLabel()
    {
        return Style.create()
                .withTextFont(fontNormal())
                .withTextColor(TEXT);
    }

    @Override
    public Style styleList()
    {
        return Style.create()
                .withDrawColor(AQUA.darkened())
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleNote()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withTextColor(FADED_TEXT);
    }

    @Override
    public Style styleProgressBar()
    {
        return Style.create()
                .withTextFont(fontSmall())
                .withDrawColor(FADED_TEXT)
                .withFillColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleSelectedTab()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(colorPanel());
    }

    @Override
    public Style styleSelection()
    {
        return Style.create()
                .withDrawColor(WHITE_SMOKE)
                .withFillColor(OCEAN);
    }

    @Override
    public Style styleTab()
    {
        return Style.create()
                .withDrawColor(TEXT)
                .withFillColor(colorPanel());
    }

    @Override
    public Style styleTabHover()
    {
        return Style.create()
                .withDrawColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND.lightened());
    }

    @Override
    public Style styleTable()
    {
        return Style.create()
                .withDrawColor(AQUA.darkened())
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTableHeader()
    {
        return Style.create()
                .withDrawColor(FOSSIL)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextArea()
    {
        return Style.create()
                .withTextColor(TEXT)
                .withFillColor(DARK_BACKGROUND);
    }

    @Override
    public Style styleTextField()
    {
        return Style.create()
                .withDrawColor(PLATINUM)
                .withFillColor(MEDIUM_BACKGROUND);
    }

    @Override
    public Style styleTitle()
    {
        return Style.create()
                .withTextFont(fontNormal().deriveFont(16.0f))
                .withDrawColor(HIGHLIGHT)
                .withTextColor(HIGHLIGHT)
                .withFillColor(DARK_BACKGROUND);
    }
}
