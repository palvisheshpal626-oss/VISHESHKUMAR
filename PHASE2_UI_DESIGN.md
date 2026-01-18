# VISHESHKUMAR - Phase 2: UI Design & Theme Foundation

## Overview
Phase 2 implements a modern, clean, and premium UI design system following Material Design 3 principles.

## Design System

### Color Palette

#### Primary Colors
- **Primary**: `#2563EB` (Blue-600) - Main brand color
- **Primary Dark**: `#1E40AF` (Blue-700) - Darker variant
- **Primary Light**: `#60A5FA` (Blue-400) - Lighter variant
- **Primary Variant**: `#3B82F6` (Blue-500) - Alternative shade

#### Secondary Colors
- **Secondary**: `#8B5CF6` (Purple-500) - Accent color
- **Secondary Dark**: `#7C3AED` (Purple-600) - Darker variant
- **Secondary Light**: `#A78BFA` (Purple-400) - Lighter variant
- **Secondary Variant**: `#9333EA` (Purple-600) - Alternative shade

#### Background & Surface
- **Background**: `#F8FAFC` (Slate-50) - Main background
- **Background Secondary**: `#F1F5F9` (Slate-100) - Secondary background
- **Surface**: `#FFFFFF` (White) - Card backgrounds
- **Surface Variant**: `#F8FAFC` (Slate-50) - Alternative surfaces

#### Text Colors
- **Text Primary**: `#0F172A` (Slate-900) - Main text
- **Text Secondary**: `#64748B` (Slate-500) - Secondary text
- **Text Tertiary**: `#94A3B8` (Slate-400) - Tertiary text
- **Text on Primary**: `#FFFFFF` (White) - Text on primary color
- **Text on Secondary**: `#FFFFFF` (White) - Text on secondary color

#### Status Colors
- **Success**: `#10B981` (Green-500) - Success states
- **Warning**: `#F59E0B` (Orange-500) - Warning states
- **Error**: `#EF4444` (Red-500) - Error states
- **Info**: `#06B6D4` (Cyan-500) - Info states

#### Difficulty Levels
- **Easy**: `#10B981` (Green) - Easy difficulty
- **Medium**: `#F59E0B` (Orange) - Medium difficulty
- **Hard**: `#EF4444` (Red) - Hard difficulty

### Typography

#### Headline Styles
- **Headline1**: 32sp, medium weight, -0.01 letter spacing
- **Headline2**: 24sp, medium weight, -0.005 letter spacing
- **Headline3**: 20sp, medium weight

#### Body Styles
- **Body1**: 16sp, regular weight, 4dp line spacing
- **Body2**: 14sp, regular weight, 2dp line spacing
- **Caption**: 12sp, regular weight

#### Button Text
- **Button**: 14sp, medium weight, 0.02 letter spacing, no all caps

### Shape & Corners

#### Corner Radii
- **Small Components**: 8dp (buttons, chips)
- **Medium Components**: 12dp (cards, inputs)
- **Large Components**: 16dp (language cards)

### Spacing System

#### Padding/Margin
- **Small**: 8dp
- **Medium**: 16dp
- **Large**: 24dp

#### Component-Specific
- **Card Padding**: 16dp (default), 20dp (language cards)
- **List Item Padding**: 16dp horizontal, 12dp vertical
- **Container Padding**: 16dp (default), 24dp (large)

### Elevation

#### Card Elevations
- **Default Card**: 2dp
- **Language Card**: 4dp
- **Outlined Card**: 0dp (with 1dp stroke)

#### Button Elevations
- **Filled Button**: 2dp
- **Outlined Button**: 0dp
- **Text Button**: 0dp

### Components

#### Cards
- **Widget.VISHESHKUMAR.Card**: Standard elevated card with 12dp radius
- **Widget.VISHESHKUMAR.Card.Outlined**: Outlined card with stroke
- **Widget.VISHESHKUMAR.Card.Language**: Premium language card with 16dp radius and 4dp elevation

#### Buttons
- **Widget.VISHESHKUMAR.Button**: Primary filled button
- **Widget.VISHESHKUMAR.Button.Outlined**: Outlined button variant
- **Widget.VISHESHKUMAR.Button.Text**: Text-only button

#### Chips
- **Widget.VISHESHKUMAR.Chip**: Standard chip for categories
- **Widget.VISHESHKUMAR.Chip.Difficulty**: Smaller chip for difficulty badges

#### Progress Indicators
- **Widget.VISHESHKUMAR.ProgressBar**: Linear progress with 8dp track, 4dp corners
- **Widget.VISHESHKUMAR.ProgressBar.Circular**: Circular progress indicator

#### List Items
- **Widget.VISHESHKUMAR.ListItem**: Single-line list item (56dp min height)
- **Widget.VISHESHKUMAR.ListItem.TwoLine**: Two-line list item (72dp min height)

### Interactive States

#### Ripple Effects
- **Light Ripple**: 10% black opacity
- **Dark Ripple**: 10% white opacity

#### Overlays
- **Light Overlay**: 5% black opacity
- **Dark Overlay**: 5% white opacity

### Lock States
- **Locked**: `#94A3B8` (Slate-400) - Locked content indicator
- **Unlocked**: `#10B981` (Green-500) - Unlocked content indicator

## Design Principles

### 1. Clean & Modern
- Generous white space
- Soft shadows and elevations
- Rounded corners throughout

### 2. Good Contrast
- WCAG AA compliant text colors
- Clear visual hierarchy
- Proper color combinations

### 3. Premium Feel
- Polished color palette
- Smooth gradients and shadows
- Professional typography

### 4. Consistency
- Unified spacing system
- Consistent corner radii
- Standardized component styles

## Usage Examples

### Using Theme Colors in Layouts
```xml
<TextView
    android:textColor="?attr/colorPrimary"
    android:background="?attr/colorSurface" />
```

### Applying Card Style
```xml
<com.google.android.material.card.MaterialCardView
    style="@style/Widget.VISHESHKUMAR.Card.Language"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <!-- Card content -->
</com.google.android.material.card.MaterialCardView>
```

### Using Typography
```xml
<TextView
    android:textAppearance="@style/TextAppearance.VISHESHKUMAR.Headline2"
    android:text="Section Title" />
```

### Applying Button Style
```xml
<com.google.android.material.button.MaterialButton
    style="@style/Widget.VISHESHKUMAR.Button"
    android:text="Start Learning" />
```

## Files Created

- `res/values/colors.xml` - Complete color palette with semantic naming
- `res/values/themes.xml` - Material Design 3 theme with custom attributes
- `res/values/styles.xml` - Component styles and widget configurations

## Theme Features

### Status Bar & Navigation Bar
- Light status bar with background color
- Light navigation bar (Android O+)
- Seamless edge-to-edge design ready

### Material Components
- Material 3 base theme
- Custom shape appearances
- Typography system integration

### Accessibility
- High contrast text colors
- Minimum touch target sizes (48dp)
- Proper content descriptions ready

## Next Steps (Phase 3)

With the UI foundation in place, the next phase can include:
- Layout XML files for screens
- ViewModels for business logic
- Navigation graph
- RecyclerView adapters with these styles
- Animations and transitions

## Notes

- All colors follow Tailwind CSS color palette for consistency
- Theme uses Material 3 (Material You) components
- Backwards compatible with minSdk 24
- Ready for dark mode implementation (future phase)
- No hardcoded colors in layouts - use theme attributes
