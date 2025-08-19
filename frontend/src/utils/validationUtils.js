export const validationUtils = {
  isRequired(value) {
    return value !== null && value !== undefined && value.toString().trim() !== '';
  },

  isEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  },

  isValidLength(value, minLength, maxLength) {
    if (!value) return !minLength || minLength === 0;
    const length = value.toString().length;
    return (!minLength || length >= minLength) && (!maxLength || length <= maxLength);
  },

  isNumber(value) {
    return !isNaN(value) && !isNaN(parseFloat(value));
  },

  isInteger(value) {
    return Number.isInteger(Number(value));
  },

  isPositiveNumber(value) {
    return this.isNumber(value) && Number(value) > 0;
  },

  isInRange(value, min, max) {
    const num = Number(value);
    return this.isNumber(value) && 
           (min === undefined || num >= min) && 
           (max === undefined || num <= max);
  },

  isBoolean(value) {
    if (typeof value === 'boolean') return true;
    const stringValue = value.toString().toLowerCase();
    return ['true', 'false', '1', '0', 'yes', 'no'].includes(stringValue);
  },

  isDate(value) {
    const date = new Date(value);
    return date instanceof Date && !isNaN(date.getTime());
  },

  isUrl(url) {
    try {
      new URL(url);
      return true;
    } catch {
      return false;
    }
  },

  isIPAddress(ip) {
    const ipRegex = /^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
    return ipRegex.test(ip);
  },

  validateField(value, rules) {
    const errors = [];

    if (rules.required && !this.isRequired(value)) {
      errors.push('This field is required');
    }

    if (value && rules.email && !this.isEmail(value)) {
      errors.push('Please enter a valid email address');
    }

    if (value && rules.minLength && !this.isValidLength(value, rules.minLength)) {
      errors.push(`Minimum length is ${rules.minLength} characters`);
    }

    if (value && rules.maxLength && !this.isValidLength(value, 0, rules.maxLength)) {
      errors.push(`Maximum length is ${rules.maxLength} characters`);
    }

    if (value && rules.number && !this.isNumber(value)) {
      errors.push('Please enter a valid number');
    }

    if (value && rules.integer && !this.isInteger(value)) {
      errors.push('Please enter a valid integer');
    }

    if (value && rules.positive && !this.isPositiveNumber(value)) {
      errors.push('Please enter a positive number');
    }

    if (value && rules.min !== undefined && !this.isInRange(value, rules.min)) {
      errors.push(`Value must be at least ${rules.min}`);
    }

    if (value && rules.max !== undefined && !this.isInRange(value, undefined, rules.max)) {
      errors.push(`Value must be at most ${rules.max}`);
    }

    return errors;
  }
};